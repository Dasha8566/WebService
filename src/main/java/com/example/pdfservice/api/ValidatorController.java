package com.example.pdfservice.api;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("p")
public class ValidatorController {
    @GetMapping("{code}")
    public String show(@PathVariable("code") String code){

        return "show/?code="+code;
    }

    @GetMapping("show")
    @ResponseBody
    public String showDirect(@RequestParam String code) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Check</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Інформація:</h1>\n" +
                "\n" +
                "<div class=\"certificate\">\n" +
                "    <p>Про сертифікат:</p>\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"event\">\n" +
                "    <p>Про подію:</p>\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "<ul>\n" +
                "</ul>\n" +
                "\n" +
                "<script>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "fetch('http://localhost:8080/api/v1/certificate-management/certificates/code/"+code+"')\n" +
                "    .then(res =>{\n" +
                "        return res.json();\n" +
                "        })\n" +
                "    .then(data => {\n" +
                "\n" +
                "        const certificateDiv = document.querySelector('.certificate');\n" +
                "\n" +
                "        const properties = Object.keys(data);\n" +
                "\n" +
                "        const eventId =data.eventId;\n" +
                "\n" +
                "\n" +
                "        properties.forEach(property => {\n" +
                "            const value = data[property];\n" +
                "            const element = document.createElement('div');\n" +
                "            element.textContent = `${property}: ${value}`;\n" +
                "            certificateDiv.appendChild(element);\n" +
                "\n" +
                "        })\n" +
                "\n" +
                "        fetch('http://localhost:8080/api/v1/event-management/events/id/'+eventId+'')\n" +
                "            .then(res2 =>{\n" +
                "                return res2.json();\n" +
                "            })\n" +
                "            .then(data2 => {\n" +
                "\n" +
                "                const eventDiv = document.querySelector('.event');\n" +
                "\n" +
                "                const properties = Object.keys(data2);\n" +
                "                properties.forEach(property => {\n" +
                "                    const value = data2[property];\n" +
                "                    const element = document.createElement('div');\n" +
                "                    element.textContent = `${property}: ${value}`;\n" +
                "                    eventDiv.appendChild(element);\n" +
                "\n" +
                "                })\n" +
                "\n" +
                "\n" +
                "            })\n" +
                "    });\n" +
                "</script>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }

}
