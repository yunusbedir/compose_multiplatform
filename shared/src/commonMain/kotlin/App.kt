import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.BirdImage

@Composable
fun App() {
    MaterialTheme {
        val birdsViewModel: BirdsViewModel =
            getViewModel(Unit, viewModelFactory { BirdsViewModel() })
        BirdsPage(
            viewModel = birdsViewModel
        )

    }
}

@Composable
fun BirdsPage(viewModel: BirdsViewModel) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            uiState.categories.forEach { category ->
                Button(
                    onClick = {
                        viewModel.selectCategory(category)
                    }
                ) {
                    Text(category)
                }
            }
        }

        AnimatedVisibility(uiState.selectedImages.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 5.dp),
                content =
                {
                    items(uiState.selectedImages) {
                        BirdImageCell(it)
                    }
                })
        }
    }
}

@Composable
fun BirdImageCell(birdImage: BirdImage) {
    KamelImage(
        asyncPainterResource("http://sebi.io/demo-image-api/${birdImage.path}"),
        "${birdImage.category} by ${birdImage.author}",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth()
            .aspectRatio(1.0f)
    )
}

expect fun getPlatformName(): String