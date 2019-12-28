package com.example.al3ra8e.hucalendar.connection;
public interface AccessLinks {
   //String DOMAIN ="192.168.8.102:82" ; //WIFI
   String DOMAIN ="192.168.43.103" ; //Local
//   String DOMAIN ="qasim.hmd2.net" ; // GLOBAL

   String EVENTS_4_CALENDAR    = "http://"+DOMAIN+"/huCalendar/search/getEventsForCalendar.php" ;
   String EVENTS_4_CATEGORY    = "http://"+DOMAIN+"/huCalendar/search/getEventsForCategory.php" ;
   String EVENTS_4_FACULTY     = "http://"+DOMAIN+"/huCalendar/search/getEventsForFaculty.php" ;
   String NOT_ACCEPTED_EVENT   = "http://"+DOMAIN+"/huCalendar/event/getNotAcceptedEvents.php" ;
   String EVENT_BY_CREATOR     = "http://"+DOMAIN+"/huCalendar/event/getEventsByCreatorId.php" ;
   String LOG_IN               = "http://"+DOMAIN+"/huCalendar/logIn.php" ;
   String SIGNIN4STUDENT       = "http://"+DOMAIN+"/huCalendar/signUp.php" ;
   String PHOTOS_DIRECTORY     = "http://"+DOMAIN+"/huCalendar/photos/" ;
   String MATERIALS_DIRECTORY  = "http://"+DOMAIN+"/huCalendar/materials/" ;
   String UPLOAD_MATERIAL      = "http://"+DOMAIN+"/huCalendar/event/uploadMaterial.php" ;
   String GET_EVENT_INFO       = "http://"+DOMAIN+"/huCalendar/event/getEventPicture.php" ;
   String EVENT_COMMENTS       = "http://"+DOMAIN+"/huCalendar/event/getAllCommentForEvent.php" ;
   String STUDENT_INFO_LINK    = "http://"+DOMAIN+"/huCalendar/student/getStudentInfoById.php" ;
   String ADD_COMMENT          = "http://"+DOMAIN+"/huCalendar/addComment.php" ;
   String ADD_EVENT            = "http://"+DOMAIN+"/huCalendar/event/addEvent.php" ;
   String ATTEND_NUMBER        = "http://"+DOMAIN+"/huCalendar/getNumberOfAttendForEvent.php"  ;
   String ATTEND_TO_EVENT      = "http://"+DOMAIN+"/huCalendar/event/attendToEvent.php" ;
   String ACCEPT_EVENT         = "http://"+DOMAIN+"/huCalendar/event/acceptEvent.php" ;
   String IS_ATTENDED          = "http://"+DOMAIN+"/huCalendar/event/isAttend.php" ;
   String IS_ACCEPTED          = "http://"+DOMAIN+"/huCalendar/event/isAccepted.php" ;
   String ATTENDED_EVENTS      = "http://"+DOMAIN+"/huCalendar/student/getAttendedEventsForStudent.php" ;
   String UPLOAD_IMAGE         = "http://"+DOMAIN+"/huCalendar/test.php";
   String GET_EVENT_MATERIALS  = "http://"+DOMAIN+"/huCalendar/event/getMaterialsForEvent.php" ;
   String GET_ALL_NEW_COMMENTS = "http://"+DOMAIN+"/huCalendar/event/getAllNewComment.php" ;
   String GET_ALL_NEW_EVENTS   = "http://"+DOMAIN+"/huCalendar/event/getAllNewEvents.php" ;
   String GET_ALL_NEW_REQUESTS = "http://"+DOMAIN+"/huCalendar/event/getAllNewEventsRequest.php" ;
   String GET_LAST_REQUEST     = "http://"+DOMAIN+"/huCalendar/event/getLastRequestEvent.php" ;



}
