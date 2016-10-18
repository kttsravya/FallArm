# FallArm
Software Engineer, Android | Fall Arm | code	2016
Mobile alert system designed to call for help when hospital outpatient mobile device detects irregular movement pattern.
Dynamically generated accelerometer and gyroscope orientation data (6 numbers) from emulated mobile Android device using Java, Android Jelly Bean, sending them to Tomcat web server.
Engineered algorithm in Java to detect the type of motion of the mobile phone by considering calibration, sensor values and calculated G-force, sending SMS message to nearest hospital if value is < 1g.
