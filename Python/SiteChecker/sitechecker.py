import requests
import emailsender


def check_server_status(url):
    try:
        response = requests.get(url, timeout=10)
        if response.status_code == 200:
            print("Server is up and running!")
        else:
            print("Server is not responding. Status code:", response.status_code)
    except requests.ConnectionError:
        print("Server is down or unreachable.")
    except requests.Timeout:
        print("Timeout occurred. Server took too long to respond.")

# Example usage
server_url = "https://christianmacarthur.com/"  # Replace with your server URL
check_server_status(server_url)

# if(is_up):
#     sendEmail --host="debugmail.io" --login="john.doe@example.org" --password="very secret" --subject="Test subject" --text="Test message" --html="<h1>Wow, so electronic, very mail</h1>"
# else:
#     print("Server is down")