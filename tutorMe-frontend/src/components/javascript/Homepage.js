import axios from 'axios';
import config from '../../../config/index';

let frontendUrl = 'http://' + config.dev.host + ":" + config.dev.port;
let backendUrl = 'https://cors-anywhere.herokuapp.com/' + 'http://' +  config.dev.backendHost;

let AXIOS = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
});

export default {
    data() {
        return {
			events: [      {
        start: '2019-11-19 10:35',
        end: '2019-11-19 11:30',
        title: 'Doctor appointment'
      }],
            lessons: null
        }
    },
    mounted: function() {
		var self = this; 
        AXIOS.get('/api/lesson/getall').then((response => {
			this.lessons = response.data; 
				this.lessons.forEach(lesson=>{
					let newevent={};
					newevent.start=lesson.startTime;
					newevent.end=lesson.endTime;
					newevent.title="lesson #"+lesson.lessonId;
					self.events.push(newevent);
					console.dir(self.events);
		})
		}));
    }, 
    methods: {
		
			
		}
        

		}