import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.dentalfirst.R
import com.example.dentalfirst.models.DestinationType
import com.example.dentalfirst.models.FulfillmentAddress
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Address
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.runtime.Error

@Composable
fun MapAddressSelectionScreen(
    onAddressSelected: (FulfillmentAddress) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mapView = rememberMapViewWithLifecycle()
    val searchManager = remember { SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED) }

    // Состояния для хранения данных
    var currentAddress by remember { mutableStateOf<FulfillmentAddress>(FulfillmentAddress.None) }
    var searchSession by remember { mutableStateOf<Session?>(null) }
    var isSearching by remember { mutableStateOf(false) }

    // 1. Создаем слушателя камеры (держим ссылку через remember)
    val cameraListener = remember {
        CameraListener { _, cameraPosition, _, finished ->
            if (finished) {
                isSearching = true
                searchSession?.cancel() // Отменяем старый поиск, если он еще идет

                searchSession = searchManager.submit(
                    cameraPosition.target,
                    20, // Зум для точности (до дома)
                    SearchOptions().apply { searchTypes = SearchType.GEO.value },
                    object : Session.SearchListener {
                        override fun onSearchResponse(response: Response) {
                            isSearching = false
                            val searchResult = response.collection.children.firstOrNull()?.obj
                            val toponymMetadata =
                                searchResult?.metadataContainer?.getItem(ToponymObjectMetadata::class.java)

                            val addressObj = toponymMetadata?.address
                            val components = addressObj?.components

                            // Извлекаем компоненты
                            val country =
                                components?.find { it.kinds.contains(Address.Component.Kind.COUNTRY) }?.name.orEmpty()
                            val city =
                                components?.find { it.kinds.contains(Address.Component.Kind.LOCALITY) }?.name.orEmpty()
                            val street =
                                components?.find { it.kinds.contains(Address.Component.Kind.STREET) }?.name
                            val house =
                                components?.find { it.kinds.contains(Address.Component.Kind.HOUSE) }?.name

                            val displayAddress =
                                if (street != null && house != null) "$street $house" else ""

                            currentAddress = FulfillmentAddress(
                                address = displayAddress,
                                country = country,
                                city = city,
                                destinationType = DestinationType.MOSCOW
                            )
                        }

                        override fun onSearchError(error: Error) {
                            isSearching = false
                            Log.e(
                                "MAP_ERROR",
                                "Search error: $error"
                            )
                        }
                    }
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 2. Карта
        AndroidView(
            factory = {
                mapView.apply {
                    mapWindow.map.apply {
                        // Начальная позиция
                        move(CameraPosition(Point(55.751244, 37.618423), 16f, 0f, 0f))
                        // Добавляем слушателя один раз при создании
                        addCameraListener(cameraListener)
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { /* Блок пуст, чтобы не перезатирать настройки при рекомпозиции */ }
        )

        // 2. Верхняя панель с адресом
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = 50.dp,
                    start = 20.dp,
                    end = 20.dp
                )
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Text(
                text = if (currentAddress != FulfillmentAddress.None)
                    currentAddress.address else "Поиск..",
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.pin),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-24).dp),
        )

        // 4. Кнопка выбора внизу
        Button(
            onClick = {
                onAddressSelected(currentAddress)
                      },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .height(56.dp)
                .fillMaxWidth(0.85f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                "Выбрать",
                color = Color.White
            )
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context)
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(
        lifecycle,
        mapView
    ) {
        val lifecycleObserver = getMapLifecycleObserver(mapView)
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

private fun getMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                MapKitFactory.getInstance().onStart()
                mapView.onStart()
            }

            Lifecycle.Event.ON_STOP -> {
                mapView.onStop()
                MapKitFactory.getInstance().onStop()
            }

            else -> {}
        }
    }