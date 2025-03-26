importScripts('https://www.gstatic.com/firebasejs/9.6.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.6.1/firebase-messaging-compat.js');

firebase.initializeApp({
    apiKey: "AIzaSyDwXET5Trnd8qISltIiDV90diUFAkp7oB8",
    authDomain: "dishcovery-4c1ac.firebaseapp.com",
    projectId: "dishcovery-4c1ac",
    storageBucket: "dishcovery-4c1ac.firebasestorage.app",
    messagingSenderId: "623301372679",
    appId: "1:623301372679:web:eaebf424a7f9df9c7573a2",
    measurementId: "G-MZFVYJWFFN",
    vapidKey: "Ctw4jHVCnl-KIEBDf4x6XVRg1DWdMBeVU7OfRm6GKCU"
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
  console.log('Received background message: ', payload);
  self.registration.showNotification(payload.notification.title, {
    body: payload.notification.body,
    //icon: '/assets/icons/icon-192x192.png'
  });
});