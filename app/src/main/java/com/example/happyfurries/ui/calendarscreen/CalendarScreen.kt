package com.example.happyfurries.ui.calendarscreen
//pantalla principal
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.happyfurries.R
import com.example.happyfurries.navigation.Routes
import com.example.happyfurries.ui.calendar.CalendarState
import com.example.happyfurries.ui.calendar.CalendarView
import java.time.YearMonth

@Composable
fun CalendarScreen(navController: NavController) {
    val pets = listOf("Misha", "Luna", "Toby") // Temporal
//Estructura base de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        //Logo app arriba

        AppLogoHeader()

        Spacer(modifier=Modifier.height(16.dp))
        //Place holder calendario

        Calendar()

        Spacer(modifier = Modifier.height(24.dp))

        UpcomingEventsSection()
        // Tira eventos
        Spacer(modifier = Modifier.height(24.dp))

        PetRow(
            pets = pets,
            onPetClick = { petName ->
                navController.navigate(Routes.PET_DETAIL)
            },
            onAddPetClick = {
                navController.navigate(Routes.ADD_PET)
            }
        ) //caja horizontal mascotas
    }
}

@Composable
fun AppLogoHeader (){
    //Logo en drawable
    Image(
        painter = painterResource(id = R.drawable.logobig),
        contentDescription = "Logo",
        modifier = Modifier.size(40.dp)
    )

}

@Composable
fun Calendar() {
    val state = remember {
        mutableStateOf(
            CalendarState(currentMonth = YearMonth.now())
        )
    }

    CalendarView(
        state = state.value,
        onDateSelected = { date ->
            state.value = state.value.copy(selectedDate = date)
        }
    )
}




@Composable
fun UpcomingEventsSection(){
    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = "Proximos Eventos",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(Color(0xFFF0F0F0)),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Proximos eventos",
                color = Color.Gray
            )


        }
    }
}


@Composable
fun PetRow(
    pets: List<String>,
    onPetClick: (String) -> Unit,
    onAddPetClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        pets.forEach { petName ->
            PetCircle(
                letter = petName.first().uppercase(),
                onClick = { onPetClick(petName) }
            )
        }

        AddPetCircle(onClick = onAddPetClick)
    }
}

@Composable
fun PetCircle(letter: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Color(0xFFE0E0E0))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
@Composable
fun AddPetCircle(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Color(0xFFD0D0D0))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+",
            style = MaterialTheme.typography.titleMedium
        )
    }
}









