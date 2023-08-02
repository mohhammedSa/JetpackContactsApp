package com.example.contactsapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.contactsapp.BackEnd.SQLiteDB
import com.example.contactsapp.ui.theme.ContactsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactsAppTheme {
                MyApp()
            }
        }
    }
}


@SuppressLint("MutableCollectionMutableState")
@Composable
fun MyApp() {
    val ctx = LocalContext.current
    var isAlertShow by rememberSaveable { mutableStateOf(false) }
    val db = SQLiteDB(ctx)

    var contactList by rememberSaveable { mutableStateOf(db.readData()) }
    fun deleteContactFunction(contact: ContactData) {
        db.deleteContact(contact.id)
        contactList = db.readData()
    }

    fun updateContactFunction(contactData: ContactData) {
        db.updateContact(contactData)
        contactList = db.readData()
    }
    Surface() {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                Title()
                MyLazyColumn(
                    list = contactList,
                    onDelete = ::deleteContactFunction,
                    onUpdateData = ::updateContactFunction
                )
            }
            MyFloatButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                isAlertShow = true
            }

            if (isAlertShow) AlertDialog(
                onConfirmClick = {
                    isAlertShow = false
                    db.addContact(it)
                    contactList = db.readData()
                }, onCancelClick = {
                    isAlertShow = false
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAlert(
    onUpdate: (updatedContact: ContactData) -> Unit,
    onCancelUpdate: () -> Unit
) {
    var updateName by rememberSaveable { mutableStateOf("") }
    var updateNumber by rememberSaveable { mutableStateOf("") }
    Dialog(
        onDismissRequest = onCancelUpdate
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "",
                    tint = Color.Red,
                    modifier = Modifier
                        .clickable(onClick = onCancelUpdate)
                        .align(Alignment.End)
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Update Contact",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(top = 5.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    value = updateName,
                    onValueChange = {
                        updateName = it
                    },
                    label = { Text(text = "Updated Name") })
                Spacer(modifier = Modifier.padding(top = 5.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    value = updateNumber,
                    onValueChange = {
                        updateNumber = it
                    },
                    label = { Text(text = "Updated Number") })
                Spacer(modifier = Modifier.padding(top = 5.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(onClick = onCancelUpdate) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.padding(start = 5.dp))
                    Button(onClick = {
                        if (updateName.isNotBlank() && updateNumber.isNotBlank())
                            onUpdate(ContactData(0, updateName, updateNumber))
                    }) {
                        Text(text = "Update")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialog(
    onConfirmClick: (ContactData) -> Unit,
    onCancelClick: () -> Unit
) {
    var contactName by rememberSaveable { mutableStateOf("") }
    var contactNumber by rememberSaveable { mutableStateOf("") }
    Dialog(
        onDismissRequest = onCancelClick
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier
                        .clickable(onClick = onCancelClick)
                        .align(Alignment.End),
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "Add Contact Alert",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                OutlinedTextField(
                    value = contactName,
                    onValueChange = { contactName = it },
                    label = { Text(text = "Name Field...") }
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                OutlinedTextField(
                    value = contactNumber,
                    onValueChange = { contactNumber = it },
                    label = { Text(text = "Number Field...") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onCancelClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.padding(start = 5.dp))
                    Button(
                        onClick = {
                            if (contactName.isNotBlank() && contactNumber.isNotBlank()) {
                                onConfirmClick(ContactData(0, contactName, contactNumber))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green
                        )
                    ) {
                        Text(text = "Add")
                    }
                }
            }
        }
    }
}

@Composable
fun MyFloatButton(modifier: Modifier, click: () -> Unit) {
    FloatingActionButton(
        modifier = modifier.size(50.dp),
        onClick = click
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
fun Title() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            text = "Contacts", textAlign = TextAlign.Center,
            fontSize = 22.sp, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MyLazyColumn(
    list: ArrayList<ContactData>,
    onDelete: (ContactData) -> Unit,
    onUpdateData: (ContactData) -> Unit
) {
    var idClickableItem by rememberSaveable { mutableStateOf(0) }
    var isUpdateAlertShow by rememberSaveable { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        content = {
            items(list) { item ->
                MyContactRow(data = item,
                    deleteClick = {
                        onDelete(item)
                    },
                    updateClick = {
                        isUpdateAlertShow = true
                        idClickableItem = item.id
                    }
                )
                if (isUpdateAlertShow) {
                    UpdateAlert(onUpdate = {
                        isUpdateAlertShow = false
                        onUpdateData(ContactData(idClickableItem, it.name, it.phNumber))
                    }, onCancelUpdate = {
                        isUpdateAlertShow = false
                    })
                }
            }
        })
}


@Composable
fun MyContactRow(data: ContactData, deleteClick: () -> Unit, updateClick: () -> Unit) {
    Surface(
        modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = data.name,
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = data.phNumber,
                    color = MaterialTheme.colorScheme.background
                )
            }
            Row {
                IconButton(onClick = deleteClick)
                {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "",
                        tint = Color.Red
                    )
                }
                IconButton(onClick = updateClick)
                {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "",
                        tint = Color.Green
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContactsAppTheme {
        UpdateAlert(onUpdate = {}) {}
    }
}