package com.eliashansean.ucsdwaterstations.ui.send

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.beust.klaxon.Klaxon
import com.eliashansean.ucsdwaterstations.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import java.net.URL
import java.util.concurrent.Executors

class SendFragment : Fragment() {

    private lateinit var sendViewModel: SendViewModel
    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sendViewModel =
            ViewModelProviders.of(this).get(SendViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_send, container, false)
        //val textView: TextView = root.findViewById(R.id.text_send)

        var json: String = ""

        doAsync(executorService = Executors.newScheduledThreadPool(5)) {
            json = URL("http://ucsd-food-and-water.s3-website-us-west-1.amazonaws.com/scraper/water_stations.json").readText()
        }

        // get list of water fountains
        Log.wtf("JOE MAMA", json)
        val result = Klaxon().parse<WaterFountain>(json)

        //Log.d("JOE MAMA", result)
        val googleMapView = root.findViewById<MapView>(R.id.mapView)

        googleMapView.onCreate(savedInstanceState)
        googleMapView.getMapAsync(OnMapReadyCallback {
            googleMap = it

            val ucsd = LatLng(32.87516, -117.23665)
            googleMap.addMarker(MarkerOptions().position(ucsd).title("UCSD FUCK KOTLIN"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ucsd, 20f))
        })

        return root
    }

}

class WaterFountain (val name: String, val latlong: List<Float>) {}
