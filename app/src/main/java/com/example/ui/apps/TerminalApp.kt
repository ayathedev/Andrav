package com.example.ui.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.TextPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TerminalLog(
    val prompt: String = "chronos@chromeos:~$",
    val command: String,
    val output: String
)

@Composable
fun TerminalApp(
    modifier: Modifier = Modifier
) {
    val logs = remember {
        mutableStateListOf(
            TerminalLog(
                command = "neofetch",
                output = getNeofetchOutput()
            )
        )
    }

    var inputCommand by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    fun executeCommand(cmd: String) {
        val trimmed = cmd.trim()
        if (trimmed.isEmpty()) return

        val outputText = when (trimmed.lowercase()) {
            "help" -> """
                Available Crostini Terminal Commands:
                  help        - Display list of supported Linux shell commands
                  neofetch    - Renders retro ASCII logo and system telemetry specs
                  crostini    - Displays Linux container status and VM stats
                  ls          - List directory contents
                  whoami      - Print effective user name
                  uname -a    - Print system information
                  ping        - Test network latency trace
                  date        - Print system date and time
                  clear       - Clear terminal screen log
                  echo [text] - Output text back to stdout
            """.trimIndent()

            "neofetch" -> getNeofetchOutput()

            "crostini" -> """
                [Crostini Linux Container Engine v4.2]
                Status: ACTIVE (Termina VM running)
                IP Address: 100.115.92.194
                Subsystem: Debian 12 (Bookworm x86_64)
                CPU Cores: 8 Virtual Cores Assigned
                Memory Allocation: 4096 MB / 8192 MB
                Storage Mounted: /mnt/chromeos/MyFiles
            """.trimIndent()

            "ls" -> """
                Desktop/  Documents/  Downloads/  Music/  Pictures/  Projects/  Videos/  .bashrc
            """.trimIndent()

            "whoami" -> "chronos (ChromeOS Root User)"

            "uname -a" -> "Linux chromeos 6.6.21-aya-tablet #1 SMP PREEMPT x86_64 GNU/Linux"

            "ping" -> """
                PING google.com (142.250.190.46) 56(84) bytes of data.
                64 bytes from 142.250.190.46: icmp_seq=1 ttl=118 time=12.4 ms
                64 bytes from 142.250.190.46: icmp_seq=2 ttl=118 time=11.8 ms
                64 bytes from 142.250.190.46: icmp_seq=3 ttl=118 time=13.1 ms
                --- google.com ping statistics ---
                3 packets transmitted, 3 received, 0% packet loss, time 2003ms
            """.trimIndent()

            "date" -> SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US).format(Date())

            "clear" -> {
                logs.clear()
                inputCommand = ""
                return
            }

            else -> {
                if (trimmed.startsWith("echo ", ignoreCase = true)) {
                    trimmed.substring(5)
                } else {
                    "bash: $trimmed: command not found. Type 'help' for available commands."
                }
            }
        }

        logs.add(TerminalLog(command = trimmed, output = outputText))
        inputCommand = ""
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B0914))
            .padding(12.dp)
    ) {
        // Log Output List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(logs) { log ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = log.prompt,
                            color = NeonCyan,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = log.command,
                            color = TextPrimary,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                    }

                    if (log.output.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = log.output,
                            color = Color(0xFFC0CAF5),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Terminal Prompt Input Line
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF141024), RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFF2E264D), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "chronos@chromeos:~$",
                color = NeonCyan,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = inputCommand,
                onValueChange = { inputCommand = it },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { executeCommand(inputCommand) }),
                modifier = Modifier
                    .weight(1f)
                    .testTag("terminal_input_field")
            )
        }
    }
}

fun getNeofetchOutput(): String {
    return """
       _  _____ ____  _____ 
      / \|  ___/ ___||  ___|
     / _ \ |_  \___ \|  |_  
    / ___ \  _| ___) |  _|  
   /_/   \_\_| |____/|_|    
   
   chronos@chromeos
   ----------------
   OS: Aya OS Tablet Edition x86_64
   Host: ChromeOS Convertible Tablet
   Kernel: 6.6.21-aya-tablet
   Uptime: 4 hours, 12 mins
   Packages: 1420 (dpkg), 12 (flatpak)
   Shell: Crostini zsh 5.9
   Resolution: 2560x1600 (Tablet Landscape)
   DE: ChromeOS Desktop Shelf Manager
   WM: Aya Window Compositor
   CPU: Octa-core ARM64 / x86_64 (8) @ 2.80GHz
   GPU: Adreno Cyber GFX 740
   Memory: 4120MiB / 8192MiB
    """.trimIndent()
}
