# JDBC-HOSPITAL-MANAGEMENT-SYSTEM
## Description
This is a console-based Hospital Management System built using Java and JDBC. It allows users to manage patients, doctors, and appointments. Admin users can add or remove doctors, while regular users can view doctors, add patients, and book appointments.

The project uses MySQL as the backend database and enforces constraints such as foreign keys to maintain data integrity.

## Features

### User Features:
- Add a new patient
- View all patients
- View all doctors
- Book an appointment with a doctor

### Admin Features:
- Login with admin credentials
- Add new doctors
- Remove doctors (cannot delete if the doctor has appointments)
- View all doctors


## Compilation and Running
# Compile
javac -cp "lib/mysql-connector-j-9.4.0.jar" -d out src/hospitalmanagementsystem/*.java

# Run (Windows)
java -cp "out;lib/mysql-connector-j-9.4.0.jar" hospitalmanagementsystem.HospitalManagementSystem

# Run (Linux/Mac)
java -cp "out:lib/mysql-connector-j-9.4.0.jar" hospitalmanagementsystem.HospitalManagementSystem
