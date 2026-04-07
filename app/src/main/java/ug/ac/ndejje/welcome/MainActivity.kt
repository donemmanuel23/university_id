package ug.ac.ndejje.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.welcome.ui.theme.StudentsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentsAppTheme {
                StudentDirectory()
            }
        }
    }
}

@Composable
fun StudentInfo(student: Student) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Surface(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .size(100.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Image(
                    painter = painterResource(id = student.profileImageId),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            // Verified Badge Overlay
            if (student.isVerified) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(24.dp),
                    shape = CircleShape,
                    color = Color(0xFF4CAF50),
                    tonalElevation = 4.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Verified",
                        tint = Color.White,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = student.name,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = student.regNumber,
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun StudentIdCard(student: Student, onViewProfile: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, bottom = 12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StudentInfo(student)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onViewProfile,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("View Profile", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailView(student: Student, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Student Profile", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "Student Details:\nName: ${student.name}\nReg No: ${student.regNumber}\nProgramme: ${student.programme}")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Student Details"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(160.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Image(
                    painter = painterResource(id = student.profileImageId),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = student.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = if (student.isVerified) "Verified Student" else "Unverified",
                color = if (student.isVerified) Color(0xFF4CAF50) else Color.Red,
                style = MaterialTheme.typography.labelLarge
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            DetailItem(
                label = "Registration Number", 
                value = student.regNumber,
                icon = Icons.Default.Person
            )
            DetailItem(
                label = "Programme of Study", 
                value = student.programme,
                icon = Icons.Default.Info
            )
            DetailItem(
                label = "System ID", 
                value = "#${student.id}",
                icon = Icons.Default.Info
            )
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            HorizontalDivider(modifier = Modifier.padding(top = 8.dp), thickness = 0.5.dp)
        }
    }
}

@Composable
fun EmptySearchResults() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Students Found",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Gray
        )
        Text(
            text = "Try searching with a different name or ID.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDirectory() {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedStudentId by rememberSaveable { mutableStateOf<Int?>(null) }
    var selectedProgramme by rememberSaveable { mutableStateOf("All") }
    var isSortedAscending by rememberSaveable { mutableStateOf(true) }
    
    val allStudents = StudentProvider.studentList
    val programmes = listOf("All", "BIT", "BCS", "BSE")

    val selectedStudent: Student? = remember(selectedStudentId) {
        allStudents.find { it.id == selectedStudentId }
    }
    
    val filteredStudents = remember(searchQuery, selectedProgramme, isSortedAscending) {
        var list = allStudents.filter {
            it.name.contains(searchQuery, ignoreCase = true) || 
            it.regNumber.contains(searchQuery, ignoreCase = true)
        }
        
        if (selectedProgramme != "All") {
            list = list.filter { it.programme == selectedProgramme }
        }
        
        if (isSortedAscending) {
            list.sortedBy { it.name }
        } else {
            list.sortedByDescending { it.name }
        }
    }

    if (selectedStudent != null) {
        StudentDetailView(
            student = selectedStudent,
            onBack = { selectedStudentId = null }
        )
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Student Directory", fontWeight = FontWeight.Bold) },
                    actions = {
                        IconButton(onClick = { isSortedAscending = !isSortedAscending }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "Sort",
                                tint = if (isSortedAscending) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Name or ID") },
                    placeholder = { Text("Enter name or reg number...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.LightGray,
                    )
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    items(programmes) { prog ->
                        FilterChip(
                            selected = selectedProgramme == prog,
                            onClick = { selectedProgramme = prog },
                            label = { Text(prog) },
                            leadingIcon = if (selectedProgramme == prog) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                            } else null
                        )
                    }
                }

                Text(
                    text = "Showing ${filteredStudents.size} students",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
                )

                if (filteredStudents.isEmpty()) {
                    EmptySearchResults()
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        items(filteredStudents) { student ->
                            StudentIdCard(
                                student = student,
                                onViewProfile = { selectedStudentId = student.id }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomePreview() {
    StudentsAppTheme {
        StudentDirectory()
    }
}
