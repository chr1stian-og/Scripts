import os
import base64
from google.auth import exceptions
from google.oauth2 import credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

# Scopes required for accessing Gmail API
SCOPES = ['https://www.googleapis.com/auth/gmail.send']

def get_authenticated_service():
    # Path to the credentials file obtained from the Google Cloud Console
    credentials_file = 'path/to/credentials.json'

    # Get credentials from the file
    creds = None
    if os.path.exists('token.json'):
        creds = credentials.Credentials.from_authorized_user_file('token.json', SCOPES)

    # If credentials are not available or expired, initiate the OAuth2 flow
    if not creds or not creds.valid:
        flow = InstalledAppFlow.from_client_secrets_file(credentials_file, SCOPES)
        creds = flow.run_local_server(port=0)
        
        # Save the credentials for future use
        with open('token.json', 'w') as token:
            token.write(creds.to_json())

    # Build the Gmail service
    service = build('gmail', 'v1', credentials=creds)
    return service


def send_email(sender_email, receiver_email, subject, message):
    # Get the authenticated Gmail service
    service = get_authenticated_service()

    # Create the email message
    email_message = MIMEMultipart()
    email_message['to'] = receiver_email
    email_message['from'] = sender_email
    email_message['subject'] = subject
    email_message.attach(MIMEText(message, 'plain'))
    raw_message = base64.urlsafe_b64encode(email_message.as_bytes()).decode('utf-8')

    try:
        # Send the email using the Gmail API
        message = service.users().messages().send(userId='me', body={'raw': raw_message}).execute()
        print('Email sent successfully. Message ID:', message['id'])
    except exceptions.GoogleAuthError as e:
        print('An error occurred while sending the email:', str(e))


# Example usage
sender_email = 'your_email@gmail.com'
receiver_email = 'recipient_email@example.com'
subject = 'Hello from Python with OAuth2'
message = 'This is a test email sent from Python using OAuth2 authentication.'

send_email(sender_email, receiver_email, subject, message)
