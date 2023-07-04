# ServiceContractApp
ServiceContract System is a service that allows you to onboard and offboard contract workers to service contracts.
It provides functionality to associate contract workers with specific service contracts and manage their allocation.

## Features

- Create Service Contracts and Contract Workers
- Onboard a contract worker to a service contract
- Offboard a contract worker from a service contract


## Technologies Used

- Java
- Spring Boot
- Docker
- H2 database
  
## Getting Started

### Prerequisites

- Java 8 or higher
- Docker

### Installation

1. Clone the repository: `git clone https://github.com/Abhishek2291995/ServiceContractApp.git`
2. Navigate to the project directory: `cd ServiceContractApp`
3. Build the project: `mvn clean install`
4. Build the Docker image: `docker build -t ServiceContractApp .`

### Usage

1. Start the Docker container: `docker run -d -p 8080:8080 ServiceContractApp`
2. Open your web browser and access the health check endpoint : [http://localhost:8080/healtcheck](http://localhost:8080/healthcheck)

## API Endpoints

The following API endpoints are available:

- `POST /contract-mappings/onBoard`: Onboard a contract worker to a service contract.
- `POST /contract-mappings/offBoard`: Offboard a contract worker from a service contract.

For detailed information on request and response formats, refer to the Swagger documentation.

## Contributing

Contributions are welcome! If you have any suggestions or improvements, feel free to submit a pull request.
