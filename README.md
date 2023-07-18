About
-------------------------------------------------------------------------------
This project is a currency exchange monitor and data display application. It retrieves exchange rates from the Narodowy Bank Polski (NBP) API and displays them in a tabular format. Additionally, it fetches data from a specific URL and provides filtering capabilities for the obtained data. The application allows users to view and track currency exchange rates, search for specific data, and store the information in a file for future reference.

The main components of the project include:
- NbpDownloadData: Retrieves currency exchange rates from the NBP API.
- NbpShowData: Displays the exchange rates in a table and writes the data to a file.
- PeripheralsDownload: Fetches additional data from a specified URL.
- PeripheralsShowData: Filters and displays the fetched data based on user input.

The project leverages Java programming language, JSON processing, multithreading, and various API interactions. It aims to provide a comprehensive tool for monitoring currency exchange rates and accessing additional data from external sources.

Compilation
-------------------------------------------------------------------------------
To run the application, one package needs to be installed.

File -> Project Structure -> Libraries -> From Maven... -> org.json

After downloading the package, we compile and run the program.

Kompilacja
-------------------------------------------------------------------------------

Do uruchomienia aplikacji potrzeba doinstalować jedną paczkę.

Flie -> Project Structure -> Libraries -> From Maven... -> org.json

Po pobraniu paczki kompilujemy i uruchamiamy program. 