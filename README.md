# Test task
## App use Yandex Translator API 
### Start


**To launch the project you need:**

- Maven
- Java 8

**How to run:**

1) Download the project
2) Run ***'mvn clean install'*** from root project path
3) In "target" folder open console
4) Run java -jar translator-1.jar in console

### Testing
- Open in browser ***[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)***
- Expand tab 'Translator'
- Push ***'Try it out'*** on some method
- Set params and push ***'Execute'***
- You should see the results below

**Requests:**
 - Get */translate*
    -  **from** *String*: From what language you want to translate
    -  **to** *String*: To what language you want to translate
    -  **text** *String*: Text what you want to translate
 - Get */changeKey*
    - **key** *String*: Special key from YT API 

### Configs:
You can customize theese values(otherwise they have default values):
- ~~Base URL ***--api.url="value"***~~
- Key ***--api.key="Your key from YT"***
- ~~Encoding (UTF-8) ***--api.encoding="value"~~

for Yandex Translator API's request.

