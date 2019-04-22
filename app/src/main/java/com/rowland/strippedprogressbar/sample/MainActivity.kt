package com.rowland.strippedprogressbar.sample

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
	
	// Per Ksh/L
	val fuelPrice = 0.015f
	// In mL
	val expectedFuelAmount = 2580f
	//  In Ksh
	val expectedFuelAmountCost = fuelPrice * expectedFuelAmount
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)
		
		WaitAsyncTask().execute()
	}
	
	
	inner class WaitAsyncTask : AsyncTask<Void, Int, Void>() {
		
		override fun onPreExecute() {
		
		}
		
		override fun doInBackground(vararg params: Void): Void? {
			for (i in 0..100) {
				try {
					Thread.sleep(Random(34).nextLong(0, 100))
					publishProgress(i)
				} catch (e: InterruptedException) {
					e.printStackTrace()
				}
			}
			return null
		}
		
		override fun onPostExecute(s: Void?) {
		
		}
		
		override fun onProgressUpdate(vararg values: Int?) {
			pb_progress_striped.progress = values[0]!!
			val fuelDeliveredAmount = expectedFuelAmount * values[0]!!/100
			val fuelDeliveredCost = fuelDeliveredAmount * fuelPrice
			tv_progress_striped.text = getString(
				R.string.fuel_delivered,
				fuelDeliveredCost,
				expectedFuelAmountCost,
				fuelDeliveredAmount
			)
		}
	}
}
