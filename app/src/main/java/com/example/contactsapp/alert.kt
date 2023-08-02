package com.example.contactsapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.contactsapp.ui.theme.ContactsAppTheme


@Preview(showBackground = true)
@Composable
fun GreetingPrev() {
    ContactsAppTheme {
        MyAlertDialog(onConfirmClick = {}) {
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAlertDialog(
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
                .padding(20.dp)
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
                        .align(Alignment.End)
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
                    label = { Text(text = "Number Field...") }
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onCancelClick
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text(text = "Cancel")
                    }
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