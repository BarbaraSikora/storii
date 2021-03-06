import {Component,OnInit,ElementRef } from 'angular2/core';
import {RouteParams, Router} from 'angular2/router';
import {LogStateComponent} from '../logState/logState.component';
import {EditBarComponent} from '../editBar/editBar.component';
import { AuthenticationService }    from '../login/authentication.service';
import { AboutService }    from '../about/about.service';
import {HttpClient}           from '../../headerfct';
import { EditBarService }    from '../editBar/editBar.service';

declare var jQuery: any;
declare var vex: any;

@Component({
  selector: 'comments',
  templateUrl: `app/html/comments/comments.html`,
  directives: [LogStateComponent, EditBarComponent],
  styles:['a {cursor: pointer}'],
  providers:[EditBarService, AboutService,AuthenticationService,HttpClient]
  
})

export class CommentsComponent implements OnInit {
    defaultStoryPic = 'app/assets/files/dummyStory.jpg';
    yellowStar = 'app/assets/files/star.png';
    halfStar = 'app/assets/files/halfgreystar.png';
    grayStar = 'app/assets/files/greystar.png';
    storyName;
    name; 
    loggedIn; 
    storyid; 
    errorMessage;
    loggedInUser;
    ratings;
    
    constructor(
    private _elRef: ElementRef,
    private _router: Router,
    private _routeParams:RouteParams,
    private _authenticationService: AuthenticationService,
    private _aboutService: AboutService,
    private _editBarService: EditBarService) {  
    this.ratings = [];
    }
    
      ngOnInit():any {
        this.storyName = this._routeParams.get('storyName');    
        this.storyid = this._routeParams.get('id'); 
        this.name = this._routeParams.get('name');
        this.loggedIn = this._authenticationService.isLoggedIn();
        let self = this; 

        this._aboutService.getStoryById(this.storyid)
                            .subscribe((result) => {    
                                    if(jQuery.isEmptyObject(result)){
                                     this._router.navigate(['Error']);
                                    }else if(result) { 
                                       var rating = [];
                                        console.log(result['ratings'].length);
                                    for(var i = 0; i < result['ratings'].length; i++){
                                        rating = [];
                                           for(var j = 0; j < result['ratings'][i]['value'];j++){
                                                rating[j]=this.yellowStar;
                                            }
                                             for(var k = result['ratings'][i]['value']; k < 5; k++){
                                                rating[k]=this.grayStar;
                                            }
                                            
                                            result['ratings'][i]['stars'] = rating;
                                    }
                                    
                                    console.log(result);
                  
                                     for(var key in result['ratings']){
                                        this._aboutService.getUserById(result['ratings'][key]['ratingUser'])
                                            .subscribe((found) => {
                                             for(var user in result['ratings']){
                                                if(result['ratings'][user]['ratingUser'] == found['id']){
                                                    console.log(found['name']);
                                                    result['ratings'][user]['ratingUser'] = found['name'];
                                                }
                                             }
                                                
                                            },
                                            error => { this._router.navigate(['Error']);}); 
                                     }
                                     
                                     this.ratings = result['ratings'];
                                    }
                                    
                                    },
                                    error => { this._router.navigate(['Error']);});
                               
        
        
        //get story by id
        
        
      }
    
     gotoProfile(name){
        this._router.navigate(['Profile', { name: name }]);
      }
    
     
   gotoStory() {   
         this._router.navigate(['About', { name: this.name, storyName: this.storyName, id: this.storyid}]);
    }
      

}
