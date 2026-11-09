package com.example.ui.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FileItem
import com.example.ui.theme.ChromeGlassBorder
import com.example.ui.theme.ChromeSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class FileCategoryNav(
    val title: String,
    val icon: ImageVector,
    val count: Int
)

@Composable
fun FilesManagerApp(
    modifier: Modifier = Modifier
) {
    val navCategories = listOf(
        FileCategoryNav("My Files", Icons.Default.Folder, 14),
        FileCategoryNav("Downloads", Icons.Default.Download, 8),
        FileCategoryNav("Documents", Icons.Default.Description, 5),
        FileCategoryNav("Images", Icons.Default.Image, 12),
        FileCategoryNav("Google Drive", Icons.Default.Cloud, 42),
        FileCategoryNav("Trash", Icons.Default.Delete, 2)
    )

    var selectedNav by remember { mutableStateOf("My Files") }
    var isGridView by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFile by remember { mutableStateOf<FileItem?>(null) }

    val mockFileList = remember {
        listOf(
            FileItem("1", "AyaOS_System_Report.pdf", "4.2 MB", "Today, 10:45 AM", false, "pdf"),
            FileItem("2", "Project_Blueprint_2026.docx", "1.8 MB", "Yesterday", false, "doc"),
            FileItem("3", "Cyberpunk_Wallpaper.png", "12.5 MB", "Nov 5, 2026", false, "image"),
            FileItem("4", "Kernel_Build_Log.txt", "320 KB", "Nov 4, 2026", false, "txt"),
            FileItem("5", "Aya_OS_Tablet_Source", "Folder", "Nov 2, 2026", true, "folder"),
            FileItem("6", "UI_Design_Tokens.json", "84 KB", "Oct 28, 2026", false, "code"),
            FileItem("7", "Tablet_Interface_Mockup.png", "8.9 MB", "Oct 22, 2026", false, "image"),
            FileItem("8", "Crostini_Terminal_Backup.tar.gz", "142 MB", "Oct 15, 2026", false, "zip")
        )
    }

    val filteredFiles = mockFileList.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(ChromeSurface)
    ) {
        // Dual-pane Sidebar Navigation (Left)
        Column(
            modifier = Modifier
                .width(220.dp)
                .fillMaxHeight()
                .background(Color(0xFF161226))
                .border(width = 1.dp, color = ChromeGlassBorder)
                .padding(14.dp)
        ) {
            Text(
                text = "FILES SYSTEM",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(navCategories) { cat ->
                    val isSelected = cat.title == selectedNav
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFF2B2446) else Color.Transparent)
                            .clickable { selectedNav = cat.title }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = cat.icon,
                            contentDescription = cat.title,
                            tint = if (isSelected) NeonCyan else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = cat.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (isSelected) TextPrimary else TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${cat.count}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Storage Capacity Gauge Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF221D3B))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Storage Gauge",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { 0.35f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = NeonCyan,
                    trackColor = Color(0xFF3B335A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "45.2 GB used of 128 GB",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                )
            }
        }

        // Main File List & Details Pane (Right)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(14.dp)
        ) {
            // Header bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedNav,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 18.sp
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Filter files...", color = TextSecondary, fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF221D3B),
                        unfocusedContainerColor = Color(0xFF1E1B2E),
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = ChromeGlassBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .width(200.dp)
                        .height(38.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Toggle View button
                IconButton(
                    onClick = { isGridView = !isGridView },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF221D3B))
                ) {
                    Icon(
                        imageVector = if (isGridView) Icons.Default.List else Icons.Default.GridView,
                        contentDescription = "Toggle View",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Body content: split into list + file detail preview if selected
            Row(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    if (isGridView) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(filteredFiles) { file ->
                                FileGridItem(
                                    file = file,
                                    isSelected = selectedFile?.id == file.id,
                                    onClick = { selectedFile = file }
                                )
                            }
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(filteredFiles) { file ->
                                FileListItem(
                                    file = file,
                                    isSelected = selectedFile?.id == file.id,
                                    onClick = { selectedFile = file }
                                )
                            }
                        }
                    }
                }

                // File Detail / Previewer Panel
                if (selectedFile != null) {
                    val file = selectedFile!!
                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier
                            .width(220.dp)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF201B35))
                            .border(1.dp, ChromeGlassBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF2E274D)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getFileIcon(file.iconType),
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = file.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 13.sp
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            DetailRow("Type", if (file.isDirectory) "Folder" else file.iconType.uppercase())
                            DetailRow("Size", file.size)
                            DetailRow("Modified", file.dateModified)
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeonCyan.copy(alpha = 0.2f))
                                .border(1.dp, NeonCyan, RoundedCornerShape(10.dp))
                                .clickable { }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Open File Preview",
                                color = NeonCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FileListItem(
    file: FileItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color(0xFF2E264D) else Color(0xFF1E1933))
            .border(
                width = 1.dp,
                color = if (isSelected) NeonCyan else ChromeGlassBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = getFileIcon(file.iconType),
            contentDescription = null,
            tint = if (file.isDirectory) NeonCyan else NeonMagenta,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = file.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = file.size,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 11.sp
            ),
            modifier = Modifier.width(70.dp)
        )
        Text(
            text = file.dateModified,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
fun FileGridItem(
    file: FileItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFF2E264D) else Color(0xFF1E1933))
            .border(
                width = 1.dp,
                color = if (isSelected) NeonCyan else ChromeGlassBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Icon(
            imageVector = getFileIcon(file.iconType),
            contentDescription = null,
            tint = if (file.isDirectory) NeonCyan else NeonMagenta,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = file.name,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = file.size,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 10.sp
            )
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextSecondary, fontSize = 11.sp)
        Text(value, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

fun getFileIcon(iconType: String): ImageVector {
    return when (iconType) {
        "folder" -> Icons.Default.Folder
        "pdf" -> Icons.Default.PictureAsPdf
        "image" -> Icons.Default.Image
        "doc" -> Icons.Default.Description
        else -> Icons.Default.InsertDriveFile
    }
}
