package com.example.metanitdatabase

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metanitdatabase.ui.theme.MetanitDataBaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val owner = LocalViewModelStoreOwner.current

            owner?.let {
                val viewModel: UserViewModel = viewModel(
                    it,
                    "UserViewModel",
                    UserViewModelFactory(LocalContext.current.applicationContext as Application)
                )

                MetanitDataBaseTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Main(viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Main(vm: UserViewModel) {
    val userList by vm.userList.observeAsState(listOf())

    Column {
        OutlinedTextField(
            value =  vm.userName.value,
            label = { Text("Name") },
            modifier = Modifier
                .padding(8.dp),
            onValueChange = { vm.changeName(it) }
        )

        OutlinedTextField(
            value =  vm.userAge.intValue.toString(),
            label = { Text("Age") },
            modifier = Modifier
                .padding(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = { vm.changeAge(it) }
        )

        Button(
            onClick = { vm.addUser() },
            modifier = Modifier
                .padding(8.dp),
        ) { Text(
            text = "Add",
            fontSize = 24.sp
        ) }

        UserList(
            users = userList,
            delete = { vm.deleteUser(it) }
        )
    }
}

@Composable
fun UserList(users: List<User>, delete: (Int) -> Unit) {
    LazyColumn(
        modifier =  Modifier.fillMaxWidth()
    ) {
        item { UserTitleRow() }
        items(users) {
            user -> UserRow(user) { delete(user.id) }
        }
    }
}

@Composable
fun UserRow(user: User, onDelete: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ) {
        Text(
            text = user.id.toString(),
            modifier = Modifier.weight(0.1f),
            fontSize = 24.sp
        )
        Text(
            text = user.name,
            modifier = Modifier.weight(0.2f),
            fontSize = 24.sp
        )
        Text(
            text = user.age.toString(),
            modifier = Modifier.weight(0.2f),
            fontSize = 24.sp
        )
        Text(
            text = "Delete",
            color = Color(0xFF6650A4),
            fontSize = 24.sp,
            modifier = Modifier
                .weight(0.2f)
                .clickable { onDelete(user.id) }
        )
    }
}

@Composable
fun UserTitleRow() {
    Row(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "ID",
            color = Color.White,
            modifier = Modifier.weight(0.1f),
            fontSize = 24.sp
        )
        Text(
            text = "Name",
            color = Color.White,
            modifier = Modifier.weight(0.2f),
            fontSize = 24.sp
        )
        Text(
            text = "Age",
            color = Color.White,
            modifier = Modifier.weight(0.2f),
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.weight(0.2f))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val owner = LocalViewModelStoreOwner.current

    owner?.let {
        val viewModel: UserViewModel = viewModel(
            it,
            "UserViewModel",
            UserViewModelFactory(LocalContext.current.applicationContext as Application)
        )

        Main(viewModel)
    }
}