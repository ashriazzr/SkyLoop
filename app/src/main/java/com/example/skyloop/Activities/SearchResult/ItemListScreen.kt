package com.example.skyloop.Activities.SearchResult

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.skyloop.Domain.FlightModel
import com.example.skyloop.R
import com.example.skyloop.ViewModel.MainViewModel


@Composable
fun ItemListScreen(
    from: String,
    to: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val items by viewModel.loadFiltered(from, to).observeAsState(emptyList())
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(from, to) {
        viewModel.loadFiltered(from, to)
    }

    LaunchedEffect(items) {
        isLoading = items.isEmpty()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.darkPurple2))
            .padding(top = 36.dp, start = 16.dp, end = 16.dp)
    ) {
        val (backBtn, headerTitle, worldImg) = createRefs()
        Image(painter = painterResource(R.drawable.back),
            contentDescription = null,
            modifier = Modifier
                .clickable { onBackClick() }
                .constrainAs(backBtn) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = "Search Result",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(start = 8.dp)
                .constrainAs(headerTitle) {
                    start.linkTo(backBtn.end, margin = 8.dp)
                    top.linkTo(backBtn.top)
                    bottom.linkTo(backBtn.bottom)
                }
        )
        Image(
            painter = painterResource(R.drawable.world),
            contentDescription = null,
            modifier = Modifier.constrainAs(worldImg) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        )
    }


    //show list
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 100.dp)
        ) {
            itemsIndexed(items) { index, item ->
                FlightItem(item=item,index=index)
            }
        }
    }
}

//// Preview harus di luar fungsi composable lain
//@Preview(showBackground = true)
//@Composable
//fun FlightItemPreview() {
//    FlightItem(
//        item = FlightModel(
//            airlineLogo = "https://upload.wikimedia.org/wikipedia/commons/4/44/Air_Asia_logo.svg",
//            airlineName = "Cathay Pacific",
//            arriveTime = "2h 45m",
//            classSeat = "Business class",
//            date = "20 aug,2025",
//            from = "Korea",
//            fromShort = "JFK",
//            numberSeat = 91,
//            price = 159.1,
//            reservedSeats = "D1,B3,B3,F6,E4,D1,D5,A6",
//            time = "22:45",
//            to = "Tokyo",
//            toShort = "LAX"
//        ),
//        index = 0
//    )
//}
