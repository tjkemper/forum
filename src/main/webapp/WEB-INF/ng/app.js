/**
 * @author Taylor Kemper
 */

angular.module("ForumApp",[
                           "ui.router", 
                           "ui.bootstrap",
                           'ui.gravatar',
                           'ngAnimate',
                           'ngCookies',
                           'textAngular'
                           ]);

//angular.module("ForumApp").constant("baseUrl","http://localhost:8085/forum/");
angular.module("ForumApp").constant("userUrl","user/");
angular.module("ForumApp").constant("loginUrl","login/");
angular.module("ForumApp").constant("logoutUrl","logout/");
angular.module("ForumApp").constant("roomsUrl","rooms/");
angular.module("ForumApp").constant("roomUrl","room/");
angular.module("ForumApp").constant("messagesUrl","/messages/");
angular.module("ForumApp").constant("messageUrl","message");
angular.module("ForumApp").constant("likeUrl","/like");
angular.module("ForumApp").constant("categoryUrl", "/category");
angular.module("ForumApp").constant("updateRoomNameUrl", "/updateRoomName");
angular.module("ForumApp").constant("updateRoomDescriptionUrl", "/updateRoomDescription");


angular.module("ForumApp")
.config(function($stateProvider, $urlRouterProvider){
	
    $urlRouterProvider.otherwise('/home');
    
    $stateProvider
        .state('logoutState', {
            url: '/logout',
            controller:"LogoutCtrl"
        })
        .state('homeState', {
            url: '/home',
            templateUrl: 'ng/templates/home.html',
            controller: 'HomeCtrl as hData'
        })
        .state('allRoomsState', {
            url: '/room',
            templateUrl: 'ng/templates/allRooms.html',
            controller:'AllRoomsCtrl as aData'
        })
        .state('roomState', {
            url: '/room/:roomName',
            templateUrl: 'ng/templates/room.html',
            controller:'RoomCtrl as rData'
        });
});






