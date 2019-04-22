### Status
[![Build Status](https://travis-ci.com/RowlandOti/StrippedProgressBar.svg?branch=master)](https://travis-ci.com/RowlandOti/StrippedProgressBar)[ ![Download](https://api.bintray.com/packages/rowlandoti/maven/StrippedProgressBar/images/download.svg) ](https://bintray.com/rowlandoti/maven/StrippedProgressBar/_latestVersion)


## StrippedProgressBar

Show diagonally stripped and animated lines as the progressbar loads.

### Preview:

![Alt text](https://github.com/RowlandOti/StrippedProgressBar/blob/master/documentation/half-progress.png?raw=true "Half Progress Preview")![Alt text](https://github.com/RowlandOti/StrippedProgressBar/blob/master/documentation/full-progress.png?raw=true "Full Progress Preview")

### Download
Grab the latest version via Maven:
```xml
<dependency>
  <groupId>com.rowland.strippedprogressbar</groupId>
  <artifactId>StrippedProgressBar</artifactId>
  <version>$latest_version</version>
  <type>pom</type>
</dependency>
```
or Gradle:
```groovy
repositories {
  jcenter()
}

dependencies {
  implementation 'com.rowland.strippedprogressbar:StrippedProgressBar:$latest_version'
}
```

### How to use

>activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layout_behavior="@string/appbar_scrolling_view_behavior"
    tools:context=".MainActivity"
    tools:showIn="@layout/activity_main">


    <com.rowland.strippedprogressbar.StrippedProgressBar
        android:id="@+id/pb_progress_striped"
        android:layout_width="match_parent"
        android:layout_height="99dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:pBrand="info"
        app:pMaxProgress="100"
        app:pLineSpacing="80dp"
        app:pProgress="0"
        app:pStriped="true"
        app:pShowPercentage="true" />

    <TextView
        android:id="@+id/tv_progress_striped"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Dispensing Ksh 280/500 (2580ml)"
        android:textColor="#deffffff"
        android:textSize="24sp"
        android:layout_marginStart="6dp"
        android:layout_marginEnd="6dp"
        app:layout_constraintBottom_toBottomOf="@id/pb_progress_striped"
        app:layout_constraintStart_toStartOf="@id/pb_progress_striped"
        app:layout_constraintTop_toTopOf="@id/pb_progress_striped" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

>MainActivity.java

```java
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
```



You only need to call `pb_progress_striped.progress = values[0]!!` on `Java/Kotlin` side to be able to update the progress on the ProgressBar.
### Coming Soon

- Different Stripped ProgressBars
- Support for extreeme customized look i.e themes.

### Developers
<table>
<tr>
<td>
     <img src="https://avatars2.githubusercontent.com/u/8356008?v=4&s=150" />

     Otieno Rowland

<p align="center">
<a href = "https://github.com/rowlandoti"><img src = "http://www.iconninja.com/files/241/825/211/round-collaboration-social-github-code-circle-network-icon.svg" width="36" height = "36"/></a>
<a href = "https://twitter.com/rowlandoti"><img src = "https://www.shareicon.net/download/2016/07/06/107115_media.svg" width="36" height="36"/></a>
<a href = "https://www.linkedin.com/in/rowlandoti"><img src = "http://www.iconninja.com/files/863/607/751/network-linkedin-social-connection-circular-circle-media-icon.svg" width="36" height="36"/></a>
</p>
</td>


</tr>
  </table>

### Licence

 Copyright (c) Otieno Rowland

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
