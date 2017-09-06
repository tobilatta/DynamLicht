DynLiCo  (Dynamic Light Control)
 ==============
 
 WAGO Light Controller via MODBUS combined with Bluetooth Heart rate sensing
 
 This app searches for nearby BLE devices and connects to heart rate sensor service. 
 It shows heart rate (average pulse) and heart rate variability (beat-to-beat interval or RR data). 
 The app connects via MODBUS to a WAGO Home station and sends control commands, depending on the actual heart rate.
 Over 120bpm (green light for stress release) or under 120bpm (red light for stimulation and energizing).
 The station is used to control RGB-LED strips.

 Bluetooh BLE is supported from Android 4.3 (API level 18) onwards. 
 Read more about [Android BLE development][2].
 There is also [Android sample code][2] for GATT services.
 
 <ul>
    <li>
       <strong>Tested heart rate belts:</strong>
       <li>Mio Link</li>
    </li>
    <li>
       <strong>Android BLE devices:</strong>
       <li>Sony Xperia Z1 comcpact</li>
    </li>
 </ul>
 
 ## Use instructions
 
 Download app from the root folder in git repository
 
 Connect heart rate BLE belt on your chest or wrist. 
 Start the app and press on Bluetooth Setup. Scan for BLE devices and select heart rate sensor. 
 Heart rate data values starts to update on the screen. 
 
 For setting up the WAGO home station, check the documentation file in the root folder in git repository.
 
 ## Developed by
 
 <ul>
  <li>
    Dennis Claren (WAGO home station development)
    <li>Petrik Krajinovic (overall organisation and documentation)</li>
    <li>Tobias Latta (Android app development)</li>
  </li>
 </ul>
 
 This project is built on the BLE-Heart-rate-variability-demo by oerjanti.
 https://github.com/oerjanti/BLE-Heart-rate-variability-demo.git
 
 

