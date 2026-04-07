package ug.ac.ndejje.welcome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
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
fun StudentPhoto(student: Student, size: androidx.compose.ui.unit.Dp) {
    if (student.profileImageUri != null) {
        AsyncImage(
            model = student.profileImageUri,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = student.profileImageId ?: android.R.drawable.ic_menu_gallery),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun StudentInfo(student: Student) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.padding(top = 24.dp)) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(modifier = Modifier.padding(8.dp)) {
                    StudentPhoto(student, 84.dp)
                }
            }
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
        
        Text(text = student.name, style = MaterialTheme.typography.titleMedium)
        Text(text = student.regNumber, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun StudentIdCard(
    student: Student,
    onViewProfile: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { onDelete() })
            },
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
fun AddStudentDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, Uri?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var regNo by remember { mutableStateOf("") }
    var programme by remember { mutableStateOf("BIT") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Add New Student", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { photoLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            Text("Photo", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = regNo,
                    onValueChange = { regNo = it },
                    label = { Text("Reg Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Text("Programme", style = MaterialTheme.typography.labelMedium, modifier = Modifier.align(Alignment.Start))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("BIT", "BCS", "BSE").forEach { prog ->
                        FilterChip(
                            selected = programme == prog,
                            onClick = { programme = prog },
                            label = { Text(prog) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Button(
                        onClick = { onAdd(name, regNo, programme, imageUri); onDismiss() },
                        enabled = name.isNotBlank() && regNo.isNotBlank()
                    ) { Text("Add") }
                }
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
                }
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
                modifier = Modifier.padding(top = 16.dp).size(160.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    if (student.profileImageUri != null) {
                        AsyncImage(
                            model = student.profileImageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = student.profileImageId ?: android.R.drawable.ic_menu_gallery),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(text = student.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(
                text = if (student.isVerified) "Verified Student" else "Unverified",
                color = if (student.isVerified) Color(0xFF4CAF50) else Color.Red,
                style = MaterialTheme.typography.labelLarge
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            DetailItem(label = "Registration Number", value = student.regNumber, icon = Icons.Default.Person)
            DetailItem(label = "Programme of Study", value = student.programme, icon = Icons.Default.Info)
            DetailItem(label = "System ID", value = "#${student.id}", icon = Icons.Default.Info)
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            HorizontalDivider(modifier = Modifier.padding(top = 8.dp), thickness = 0.5.dp)
        }
    }
}

@Composable
fun EmptySearchResults() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "No Students Found", style = MaterialTheme.typography.headlineSmall, color = Color.Gray)
        Text(text = "Try searching with a different name or ID.", style = MaterialTheme.typography.bodyMedium, color = Color.LightGray, textAlign = TextAlign.Center)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDirectory() {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedStudentId by rememberSaveable { mutableStateOf<Int?>(null) }
    var selectedProgramme by rememberSaveable { mutableStateOf("All") }
    var isSortedAscending by rememberSaveable { mutableStateOf(true) }
    var showAddDialog by remember { mutableStateOf(false) }

    val studentList = remember { mutableStateListOf<Student>().apply { addAll(StudentProvider.studentList) } }
    val programmes = listOf("All", "BIT", "BCS", "BSE")

    val selectedStudent = remember(selectedStudentId, studentList.size) {
        studentList.find { it.id == selectedStudentId }
    }
    
    val filteredStudents = remember(searchQuery, selectedProgramme, isSortedAscending, studentList.size) {
        var list = studentList.filter {
            it.name.contains(searchQuery, ignoreCase = true) || it.regNumber.contains(searchQuery, ignoreCase = true)
        }
        if (selectedProgramme != "All") list = list.filter { it.programme == selectedProgramme }
        if (isSortedAscending) list.sortedBy { it.name } else list.sortedByDescending { it.name }
    }

    if (selectedStudent != null) {
        StudentDetailView(student = selectedStudent, onBack = { selectedStudentId = null })
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Student Directory", fontWeight = FontWeight.Bold) },
                    actions = {
                        IconButton(onClick = { isSortedAscending = !isSortedAscending }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "Sort", tint = if (isSortedAscending) MaterialTheme.colorScheme.primary else Color.Gray)
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Student")
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
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
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = Color.LightGray)
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

                Text(text = "Showing ${filteredStudents.size} students (Long-press to delete)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 24.dp, bottom = 8.dp) )

                if (filteredStudents.isEmpty()) {
                    EmptySearchResults()
                } else {
                    LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                        items(filteredStudents, key = { it.id }) { student ->
                            AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically()) {
                                StudentIdCard(
                                    student = student,
                                    onViewProfile = { selectedStudentId = student.id },
                                    onDelete = { studentList.remove(student) }
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddStudentDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, reg, prog, uri ->
                val newId = (studentList.maxOfOrNull { it.id } ?: 0) + 1
                studentList.add(Student(id = newId, name = name, regNumber = reg, programme = prog, profileImageUri = uri, isVerified = false))
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomePreview() {
    StudentsAppTheme { StudentDirectory() }
}
