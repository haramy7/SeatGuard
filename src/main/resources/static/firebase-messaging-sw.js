importScripts('https://www.gstatic.com/firebasejs/10.7.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.7.1/firebase-messaging-compat.js');

//설정값 (admin.html이랑 똑같아야 함)
const firebaseConfig = {
    apiKey: "AIzaSyCz3QIK51dRJxF6KodEqJ7yhh_4lI1reos",
    authDomain: "seatguard-e1ffe.firebaseapp.com",
    projectId: "seatguard-e1ffe",
    storageBucket: "seatguard-e1ffe.firebasestorage.app",
    messagingSenderId: "182765965666",
    appId: "1:182765965666:web:03d7210d1c5f5e1f5bdc3b",
    measurementId: "G-3LFL80GCQC"
};

firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

messaging.onBackgroundMessage(function(payload) {
  console.log('[firebase-messaging-sw.js] Received background message ', payload);
  const notificationTitle = payload.notification.title;
  const notificationOptions = {
    body: payload.notification.body,
    icon: '/favicon.png'
  };

  self.registration.showNotification(notificationTitle, notificationOptions);
});
