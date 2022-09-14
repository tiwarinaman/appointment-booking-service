# appointment-booking-service

## Create Appointment - POST Request

* endpoint - 
  http://localhost:8080/api/v1/appointment
  
  ##### Request Body Payload

  ```json
  {
    "name": "Naman",
    "date": "2022-09-14",
    "startTime": "09:00",
    "endTime": "10:30",
    "purpose": "Just casual catch-up"
  }
  ```
 
## Get Appointment By Id - GET Request

  * endpoint - http://localhost:8080/api/v1/appointment/{Id}
  
## Update Appointment - PUT Request
  
  * endpoint - http://localhost:8080/api/v1/appointment/{id}
  
  ##### Payload
  
  ```json
   {
    "date": "2022-09-15",
    "startTime": "08:00",
    "endTime": "09:30"
   }
  ```
  
 
## Get Appointments By Date Range - GET Request

  * endpoint - http://localhost:8080/api/v1/appointment/{startDate}/{endDate}
 
## Delete Appointment By ID - DELETE Request

  * endpoint - http://localhost:8080/api/v1/appointment/{Id}

  
