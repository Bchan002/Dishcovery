import { inject, Injectable } from '@angular/core';
import { AngularFireMessaging } from '@angular/fire/compat/messaging';
import { BehaviorSubject } from 'rxjs';
import { take } from 'rxjs/operators';

@Injectable()
export class NotificationService {

    // Create a behavioural subject where it can maintain and provide a current value to subscribers 
    private notifications = new BehaviorSubject<any[]>([])

    // Convert yp pnservable
    notification$ = this.notifications.asObservable()

    private afMessaging = inject(AngularFireMessaging)

    requestPermission() {
      this.afMessaging.requestToken.pipe(take(1)).subscribe(
        (token) => {
          console.log('User FCM Token:', token);
          localStorage.setItem('fcmToken', JSON.stringify(token));
          // Send token to backend
        },
        (error) => {
          console.error('Permission denied', error);
        }
      );
    }
    
    listenForMessages() {
        this.afMessaging.messages.subscribe((message: any) => {
          console.log('🚀 New Notification:', message);
            

            const newNotification = {
                title: message.notification?.title || 'Grocery Expiration Alert', 
                body: message.notification?.body || 'Check your Groceries', 
                time: new Date().toLocaleTimeString
            }

            // Update the notifications array with the new message 
            this.notifications.next([...this.notifications.value, newNotification])

          // Show notification inside the app
          alert(`📢 ${message.notification.title}: ${message.notification.body}`);
      
          // Create a visible notification when app is in the foreground
          if (Notification.permission === 'granted') {
            const notificationTitle = message?.notification?.title || 'Grocery Expiration Alert';
            const notificationOptions = {
              body: message?.notification?.body || 'Check your groceries!',
              //icon: '/assets/icons/icon-72x72.png'
            };
      
            // Create the notification
            const notification = new Notification(notificationTitle, notificationOptions);
      
            // Optional: Handle notification click event
            notification.onclick = () => {
              console.log('🔔 Notification clicked');
              window.focus();
              notification.close();
            };
          }
        });
    }

    clearNotifications() {
        this.notifications.next([])
    }




  }