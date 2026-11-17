package com.example.gemini.ui.settings

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gemini.theme.AppColors
import com.example.gemini.theme.GeminiBlue
import com.example.gemini.theme.GeminiPurple
import com.example.gemini.theme.QuotaGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

data class GitCommitItem(
    val sha: String,
    val message: String,
    val author: String,
    val date: String,
    val url: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevSyncDashboardScreen(
    cardBg: Color,
    cardBorder: BorderStroke,
    onBack: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val c = AppColors

    // Security check states
    // Method 1: Allowed GitHub Account
    val allowedUser = "myfunuiai-del"
    // Method 2: Secret PIN Code (Default: 420421 or 2026)
    var isUnlocked by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    // Repos state
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Official (Santoshkurmi), 1: Aura (Akash420421)
    var isLoadingOfficial by remember { mutableStateOf(false) }
    var isLoadingAura by remember { mutableStateOf(false) }
    var officialCommits by remember { mutableStateOf<List<GitCommitItem>>(emptyList()) }
    var auraCommits by remember { mutableStateOf<List<GitCommitItem>>(emptyList()) }
    var officialError by remember { mutableStateOf<String?>(null) }
    var auraError by remember { mutableStateOf<String?>(null) }

    fun fetchCommits(repo: String, onDone: (List<GitCommitItem>?, String?) -> Unit) {
        coroutineScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val url = URL("https://api.github.com/repos/$repo/commits?per_page=20")
                    val conn = url.openConnection() as HttpURLConnection
                    conn.setRequestProperty("User-Agent", "AntiGem-DevSync-App")
                    conn.connectTimeout = 8000
                    conn.readTimeout = 8000
                    if (conn.responseCode in 200..299) {
                        val reader = BufferedReader(InputStreamReader(conn.inputStream))
                        val json = reader.readText()
                        reader.close()
                        val array = JSONArray(json)
                        val list = mutableListOf<GitCommitItem>()
                        for (i in 0 until array.length()) {
                            val obj = array.getJSONObject(i)
                            val sha = obj.optString("sha", "").take(7)
                            val commitObj = obj.optJSONObject("commit")
                            val message = commitObj?.optString("message", "")?.lines()?.firstOrNull() ?: ""
                            val author = commitObj?.optJSONObject("author")?.optString("name", "Unknown") ?: "Unknown"
                            val date = commitObj?.optJSONObject("author")?.optString("date", "")?.take(10) ?: ""
                            val htmlUrl = obj.optString("html_url", "")
                            list.add(GitCommitItem(sha, message, author, date, htmlUrl))
                        }
                        list
                    } else {
                        throw Exception("HTTP ${conn.responseCode}: ${conn.responseMessage}")
                    }
                }
                onDone(result, null)
            } catch (e: Exception) {
                onDone(null, e.localizedMessage ?: "Failed to fetch")
            }
        }
    }

    LaunchedEffect(isUnlocked) {
        if (isUnlocked) {
            isLoadingOfficial = true
            fetchCommits("Santoshkurmi/antigem") { list, err ->
                isLoadingOfficial = false
                officialCommits = list ?: emptyList()
                officialError = err
            }
            isLoadingAura = true
            fetchCommits("Akash420421/Aura-NextGen-2") { list, err ->
                isLoadingAura = false
                auraCommits = list ?: emptyList()
                auraError = err
            }
        }
    }

    if (!isUnlocked) {
        // Method 1 + 2 Security Gate
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = cardBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(GeminiPurple.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Lock, contentDescription = "Locked", tint = GeminiPurple, modifier = Modifier.size(28.dp))
                    }

                    Text(
                        "Developer Sync Authentication",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "Target Dev: $allowedUser",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = {
                            pinInput = it
                            pinError = false
                            if (it == "2026" || it == "420421") {
                                isUnlocked = true
                            }
                        },
                        label = { Text("Enter Developer Passcode") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = pinError,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            if (pinInput == "2026" || pinInput == "420421") {
                                isUnlocked = true
                            } else {
                                pinError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GeminiBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Unlock Dashboard")
                    }
                }
            }
        }
        return
    }

    // Unlocked Developer Updates Screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Repository Upstream Monitor",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Realtime sync inspector for upstream updates",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = {
                    if (selectedTab == 0) {
                        isLoadingOfficial = true
                        fetchCommits("Santoshkurmi/antigem") { list, err ->
                            isLoadingOfficial = false
                            officialCommits = list ?: emptyList()
                            officialError = err
                        }
                    } else {
                        isLoadingAura = true
                        fetchCommits("Akash420421/Aura-NextGen-2") { list, err ->
                            isLoadingAura = false
                            auraCommits = list ?: emptyList()
                            auraError = err
                        }
                    }
                }
            ) {
                Icon(Icons.Outlined.Refresh, contentDescription = "Refresh", tint = GeminiBlue)
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            divider = {}
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        "Official (Santoshkurmi)",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) GeminiBlue else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        "Aura Fork (Akash420421)",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) GeminiPurple else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }

        // Terminal Command Hint
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = if (selectedTab == 0)
                        "Terminal Check:\ngit fetch upstream && git log master..upstream/master --oneline"
                    else
                        "Terminal Check:\ngit fetch aura && git log upstream-aura..aura/master --oneline",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Commits List
        val currentLoading = if (selectedTab == 0) isLoadingOfficial else isLoadingAura
        val currentError = if (selectedTab == 0) officialError else auraError
        val currentList = if (selectedTab == 0) officialCommits else auraCommits

        if (currentLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = if (selectedTab == 0) GeminiBlue else GeminiPurple)
            }
        } else if (currentError != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: $currentError", color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currentList) { item ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = cardBorder,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = (if (selectedTab == 0) GeminiBlue else GeminiPurple).copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = item.sha,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedTab == 0) GeminiBlue else GeminiPurple,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = item.date,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = item.message,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "By ${item.author}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
