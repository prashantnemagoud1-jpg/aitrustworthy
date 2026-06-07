package com.example.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import com.example.BuildConfig

data class TrustAnalysisResult(
    val riskScore: Int,
    val threatLevel: String,
    val explanation: String,
    val redFlags: List<String>,
    val recommendedActions: List<String>,
    val finalVerdict: String
)

object GeminiClient {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val resultAdapter = moshi.adapter(TrustAnalysisResult::class.java)

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun analyzeText(type: String, input: String): TrustAnalysisResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey.startsWith("__PLACEHOLDER") || apiKey == "YOUR_GEMINI_API_KEY") {
            return@withContext performHeuristicFallback(type, input)
        }

        val escapedInput = input
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")

        val prompt = "Perform a critical cybersecurity analysis of the following text under category '$type'. Analyze details, check for spelling manipulations, high ROI claims, fee requests, fake job setups, malicious link extensions (.icu, .xyz, etc), domain spoofing, unverified investment yields, or fraudulent agreements. Input Text: \"$escapedInput\"  Respond strictly with a JSON object containing keys: 'riskScore' (integer 0-100), 'threatLevel' (SAFE/LOW/MEDIUM/HIGH/CRITICAL), 'explanation' (string), 'redFlags' (array of strings), 'recommendedActions' (array of strings), 'finalVerdict' (string summarizing scam type)."

        val requestJson = """
            {
              "contents": [{
                "parts": [{
                  "text": "$prompt"
                }]
              }],
              "generationConfig": {
                "responseMimeType": "application/json",
                "temperature": 0.2
              }
            }
        """.trimIndent()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = requestJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
            .post(body)
            .build()

        try {
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw Exception("API call failed with code ${response.code}")
                }
                val rawBody = response.body?.string() ?: throw Exception("Empty body")
                val parsedResult = extractJsonFromResponse(rawBody)
                resultAdapter.fromJson(parsedResult) ?: throw Exception("JSON conversion returned null")
            }
        } catch (e: Exception) {
            performHeuristicFallback(type, input, "Execution Error: ${e.localizedMessage}")
        }
    }

    private fun extractJsonFromResponse(responseBody: String): String {
        val candidateMarker = "\"text\":"
        val startIdx = responseBody.indexOf(candidateMarker)
        if (startIdx == -1) return responseBody
        
        val actualStart = responseBody.indexOf("\"", startIdx + candidateMarker.length)
        if (actualStart == -1) return responseBody
        
        val actualEnd = responseBody.indexOf("\"", actualStart + 1)
        if (actualEnd == -1) return responseBody

        val nestedJson = responseBody.substring(actualStart + 1, actualEnd)
        return nestedJson
            .replace("\\\"", "\"")
            .replace("\\n", "\n")
            .replace("\\\\", "\\")
    }

    private fun performHeuristicFallback(type: String, input: String, reason: String = "No API Key"): TrustAnalysisResult {
        val uppercaseInput = input.uppercase()
        var riskScore = 15
        var level = "LOW"
        var verdict = "SECURE / VERIFIED"
        val flags = mutableListOf<String>()
        val actions = mutableListOf<String>()

        when (type) {
            "Scam Message" -> {
                if (uppercaseInput.contains("PAYPAL") || uppercaseInput.contains("BANK") || uppercaseInput.contains("RECOVERY") || uppercaseInput.contains("SUSPEND")) {
                    riskScore = 85
                    flags.add("Exhibits branding credentials simulation (PayPal/Bank spoofing).")
                }
                if (uppercaseInput.contains(".ICU") || uppercaseInput.contains(".XYZ") || uppercaseInput.contains(".ZIP") || uppercaseInput.contains("/")) {
                    riskScore = riskScore.coerceAtLeast(95)
                    flags.add("Embedded threat URL redirecting to non-standard domains.")
                }
                if (uppercaseInput.contains("URGENT") || uppercaseInput.contains("CONFIRM NOW") || uppercaseInput.contains("VERIFICATION PIN")) {
                    riskScore = riskScore.coerceAtLeast(70)
                    flags.add("Exceedingly matches social engineering pressure indicators (Urgency).")
                }

                if (riskScore >= 70) {
                    level = if (riskScore >= 90) "CRITICAL" else "HIGH"
                    verdict = "PHISHING_ATTACK_SMS"
                    actions.add("Do NOT click the linked redirection portal.")
                    actions.add("Report spam directly to systemic telecom providers.")
                } else {
                    actions.add("Normal alert. Monitor sender metadata.")
                }
            }
            "Job Offer" -> {
                if (uppercaseInput.contains("DAILY") || uppercaseInput.contains("WEEKLY") || uppercaseInput.contains("$") && (uppercaseInput.contains("300") || uppercaseInput.contains("900") || uppercaseInput.contains("1000"))) {
                    riskScore = 80
                    flags.add("Exaggerated salary specifications with low skills requirement.")
                }
                if (uppercaseInput.contains("FEE") || uppercaseInput.contains("PAY FIRST") || uppercaseInput.contains("REGISTRATION") || uppercaseInput.contains("AUTHORIZATION")) {
                    riskScore = riskScore.coerceAtLeast(95)
                    flags.add("Asks for direct upfront financial deposits prior to job deployment.")
                }
                if (uppercaseInput.contains("@GMAIL") || uppercaseInput.contains("@HOTMAIL")) {
                    riskScore = riskScore.coerceAtLeast(60)
                    flags.add("Hiring manager coordinates using unverified public email addresses.")
                }

                if (riskScore >= 60) {
                    level = if (riskScore >= 90) "CRITICAL" else "HIGH"
                    verdict = "FRAUDULENT_RECRUITMENT"
                    actions.add("Refuse paying any equipment fees or deposit requirements.")
                    actions.add("Query verified official portal careers registry.")
                } else {
                    actions.add("Verify hiring channels in LinkedIn or direct company portal.")
                }
            }
            "Investment Scheme" -> {
                if (uppercaseInput.contains("GUARANTEED") || uppercaseInput.contains("NO RISK") || uppercaseInput.contains("100% SECURE")) {
                    riskScore = 85
                    flags.add("Guaranteed ROI promises which violates SEC transparency criteria.")
                }
                if (uppercaseInput.contains("REFERRAL") || uppercaseInput.contains("REFER") || uppercaseInput.contains("COMMISSION") || uppercaseInput.contains("INVITE")) {
                    riskScore = riskScore.coerceAtLeast(75)
                    flags.add("Heavy focus on recruitment commissions, indicating potential Ponzi structures.")
                }
                if (uppercaseInput.contains("CRYPTO") || uppercaseInput.contains("LIQUIDITY MINING") || uppercaseInput.contains("MULTIPLY")) {
                    riskScore = riskScore.coerceAtLeast(65)
                    flags.add("Extremely speculative token pooling mechanics lacking financial audits.")
                }

                if (riskScore >= 65) {
                    level = if (riskScore >= 90) "CRITICAL" else "HIGH"
                    verdict = "PONZI_SCHEME_BOOSTER"
                    actions.add("Terminate communication. Guaranteed returns do not exist.")
                    actions.add("Report token metrics directly to local security regulators.")
                } else {
                    actions.add("Verify financial agent with proper trade registry directories.")
                }
            }
            "Website Risk" -> {
                if (uppercaseInput.contains(".XYZ") || uppercaseInput.contains(".ICU") || uppercaseInput.contains(".ONLINE") || uppercaseInput.contains(".WEBSITE")) {
                    riskScore = 80
                    flags.add("Relies on untrusted or cheap generic top level domains (TLDs).")
                }
                if (uppercaseInput.contains("-") && (uppercaseInput.contains("SECURE") || uppercaseInput.contains("UPGRADE") || uppercaseInput.contains("LOGIN"))) {
                    riskScore = riskScore.coerceAtLeast(85)
                    flags.add("Brand spoofing using deceptive hyphen expansions (e.g. netflix-account-upgrade).")
                }

                if (riskScore >= 70) {
                    level = "HIGH"
                    verdict = "SUSPICIOUS_DOMAIN_REDIRECTIONS"
                    actions.add("Do NOT provide passwords or biometric profiles under this channel.")
                    actions.add("Block the domain in browser client firewalls.")
                } else {
                    actions.add("Safe default domain trace. Always inspect active SSL certifications.")
                }
            }
            else -> {
                if (uppercaseInput.contains("WIRE TRANSFER") || uppercaseInput.contains("BTC") || uppercaseInput.contains("BITCOIN") || uppercaseInput.contains("ESTATE")) {
                    riskScore = 70
                    flags.add("Urgently demands immediate non-reversible wire transfers outside standard contracts.")
                }
                if (uppercaseInput.contains("CONFIDENTIAL") || uppercaseInput.contains("URGENT")) {
                    riskScore = riskScore.coerceAtLeast(45)
                    flags.add("Urgent language warning. Possible contract-signing pressure threat.")
                }

                if (riskScore >= 50) {
                    level = "MEDIUM"
                    verdict = "INTERMEDIATE_FRAUD_RISK"
                    actions.add("Consult real certified escrow attorneys prior to wired approvals.")
                    actions.add("Confirm account routing details over phone lines directly.")
                } else {
                    actions.add("Always cross-check signer credentials manually.")
                }
            }
        }

        return TrustAnalysisResult(
            riskScore = riskScore,
            threatLevel = level,
            explanation = "LOCAL COMPILING DISSECTOR SCAN [$reason]: $input",
            redFlags = if (flags.isEmpty()) listOf("No structural alarm markers detected.") else flags,
            recommendedActions = if (actions.isEmpty()) listOf("Monitor telemetry alerts closely.") else actions,
            finalVerdict = verdict
        )
    }
}
