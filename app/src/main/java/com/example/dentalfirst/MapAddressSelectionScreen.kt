import android.util.Log
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.dentalfirst.AddressTextField
import com.example.dentalfirst.R
import com.example.dentalfirst.components.SecondaryButton
import com.example.dentalfirst.models.DestinationType
import com.example.dentalfirst.models.FulfillmentAddress
import com.example.dentalfirst.ui.theme.DarkGrey
import com.example.dentalfirst.ui.theme.MiddleGrey
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapAddressSelectionScreen(
    lat: Double,
    long: Double,
    onAddressSelected: (FulfillmentAddress) -> Unit,
    onBackClick: () -> Unit,
    onSearch: () -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val mapView = rememberMapViewWithLifecycle()
    val searchManager =
        remember { SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED) }

    // Состояния для хранения данных
    var currentAddress by remember { mutableStateOf<FulfillmentAddress>(FulfillmentAddress.None) }
    val searchString = ""
    val notFoundString = "Не удалось определить"
    var displayAddress by remember { mutableStateOf(searchString) }
    var searchSession by remember { mutableStateOf<Session?>(null) }
    var isSearching by remember { mutableStateOf(false) }

    var showSheet by remember { mutableStateOf(false) }
    var flat by remember { mutableStateOf("") }
    var postal by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState()


    val cameraListener = remember {
        CameraListener { _, cameraPosition, _, finished ->
            if (finished) {
                isSearching = true
                displayAddress = searchString
                searchSession?.cancel()

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

                            val country =
                                components?.find { it.kinds.contains(Address.Component.Kind.COUNTRY) }?.name.orEmpty()
                            val city =
                                components?.find { it.kinds.contains(Address.Component.Kind.LOCALITY) }?.name
                            val street =
                                components?.find { it.kinds.contains(Address.Component.Kind.STREET) }?.name
                            val house =
                                components?.find { it.kinds.contains(Address.Component.Kind.HOUSE) }?.name

                            if (city != null && street != null && house != null) {
                                displayAddress = "$city, $street $house"
                                currentAddress = FulfillmentAddress(
                                    address = "$street $house",
                                    country = country,
                                    city = city,
                                    destinationType = DestinationType.MOSCOW,
                                    latitude = cameraPosition.target.latitude,
                                    longitude = cameraPosition.target.longitude
                                )
                                Log.d(
                                    "MAP",
                                    currentAddress.toString()
                                )
                            } else {
                                displayAddress = notFoundString
                                currentAddress = FulfillmentAddress.None
                            }
                        }

                        override fun onSearchError(error: Error) {
                            isSearching = false
                            displayAddress = notFoundString
                            Log.e(
                                "MAP",
                                "Search error: $error"
                            )
                        }
                    }
                )
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        AndroidView(
            factory = {
                mapView.apply {
                    mapWindow.map.apply {
                        move(
                            CameraPosition(
                                Point(
                                    lat,
                                    long
                                ),
                                16f,
                                0f,
                                0f
                            )
                        )
                        addCameraListener(cameraListener)
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = {

            }
        )

        CircleButton(
            onClick = onBackClick,
            iconRes = R.drawable.arrow_left_long_ic,
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 12.dp, horizontal = 20.dp)
                .align(Alignment.TopStart)
        )

        CircleButton(
            onClick = onSearch,
            iconRes = R.drawable.magnifer,
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 12.dp, horizontal = 20.dp)
                .align(Alignment.TopEnd)
        )

        Text(
            text = displayAddress,
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 78.dp, horizontal = 20.dp)
                .align(Alignment.TopCenter),
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp)
        )

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.pin),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-24).dp),
        )

        SecondaryButton(
            text = "Выбрать",
            onClick = {
                showSheet = true
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(innerPadding)
                .padding(20.dp)
        )

        if (showSheet) {
            AddressSelectionSheet(
                fulfillmentAddress = currentAddress,
                flat = flat,
                onFlatChange = { flat = it },
                postal = postal,
                onPostalChange = { postal = it },
                sheetState = sheetState,
                onDismiss = { showSheet = false },
                onAddressSelected = { address ->
                    onAddressSelected(address)
                    showSheet = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSelectionSheet(
    fulfillmentAddress: FulfillmentAddress,
    flat: String,
    onFlatChange: (String) -> Unit,
    postal: String,
    onPostalChange: (String) -> Unit,
    onAddressSelected: (FulfillmentAddress) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        scrimColor = Color.Transparent,
        containerColor = Color.White,
        shape = MaterialTheme.shapes.medium,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${fulfillmentAddress.country}, ${fulfillmentAddress.city}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MiddleGrey
                    )
                    Text(
                        text = fulfillmentAddress.address,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.right_arrow_ic),
                    contentDescription = null,
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Поля ввода
            AddressTextField(
                value = flat,
                onValueChange = onFlatChange,
                placeholder = "Квартира, офис",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            AddressTextField(
                value = postal,
                onValueChange = onPostalChange,
                placeholder = "Индекс",
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Кнопка подтверждения
            SecondaryButton(
                text = "Выбрать",
                onClick = {
                    onAddressSelected(
                        fulfillmentAddress.copy(
                            address =
                                fulfillmentAddress.address + ", кв." + flat + ", " + postal
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun CircleButton(
    onClick: () -> Unit,
    iconRes: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(52.dp),
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 6.dp,
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                ImageVector.vectorResource(iconRes),
                contentDescription = null,
                tint = DarkGrey,
                modifier = Modifier.size(20.dp)
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