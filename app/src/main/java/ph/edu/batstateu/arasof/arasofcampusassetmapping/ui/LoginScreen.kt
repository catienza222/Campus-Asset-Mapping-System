package ph.edu.batstateu.arasof.arasofcampusassetmapping.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ph.edu.batstateu.arasof.arasofcampusassetmapping.model.Role
import ph.edu.batstateu.arasof.arasofcampusassetmapping.viewmodel.CampusViewModel

@Composable
fun LoginScreen(vm: CampusViewModel) {
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(Role.STUDENT) }
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Campus Asset Mapping", style = MaterialTheme.typography.headlineMedium)
        Text("Sign in to continue", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Your name") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            FilterChip(selected = role == Role.STUDENT, onClick = { role = Role.STUDENT }, label = { Text("Student") })
            FilterChip(selected = role == Role.MAINTENANCE, onClick = { role = Role.MAINTENANCE }, label = { Text("Maintenance / IT") })
        }

        if (role == Role.MAINTENANCE) {
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = pin, onValueChange = { pin = it },
                label = { Text("Maintenance PIN") }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(20.dp))
        Button(onClick = { error = vm.login(name, role, pin) }, modifier = Modifier.fillMaxWidth()) {
            Text("Continue")
        }
    }
}