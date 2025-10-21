# Home Automation Server
I wanted a custom alternative to something like Home Assistant, without the bloat and working the way I want it to.
Designed to run on a cheap Raspberry Pi, but currently running in docker container on TrueNAS

## Features
* Real-time device control and telemetry via STOMP over WebSocket (with SockJS fallback)
* PostgreSQL database with version control, using Flyway
* Web UI

## Current capabilities
* Converts the regular lights in smart lights
* Controls the operation of the bathroom fan, measuring temperature and humidity
* Manages the video freed from the smart doorbell
* Measures humidity and temperature in herb pot, with the plans to enable automatic watering and grow light operation
* Controls the operation of led strips
* room temperature control by connecting to the api of other devices, for an all in one UI

## In Progress capabilities
* Smart door lock, with RFID control of the front door
