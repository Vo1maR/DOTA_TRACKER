import com.example.dotan.OpenDotaService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.opendota.com/api/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val openDotaService: OpenDotaService = retrofit.create(OpenDotaService::class.java)