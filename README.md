# Questions regarding the project and the research papers

**Question: Imagine you are new to the programming world and not proficient enough in coding. But,
you have a brilliant idea where you want to develop a context-sensing application like Project 1.
You come across the Health-Dev paper and want it to build your application. Specify what
Specifications you should provide to the Health-Dev framework to develop the code ideally. (15
points)**

Answer:
BSN – Body Sensor Network
PHMS - Pervasive Health Monitoring System
Project 1: Manually written code to use sensors from the mobile and using symptoms trying to
generate the resulting health statistics for the user.
Health-Dev: Proposes a solution to use BSN (small sensors across the body) and automatically
generates the code for both the body sensors and the smartphone that collects data from these
sensors. Health-Dev includes a library of pre-written code for common health monitoring tasks,
making it faster to develop new applications but has its own disadvantages like it generates a huge
line of code but uses less memory compared to the benchmark system.

Now coming to the key specifications:

1. Sensor specifications:
   We need to give the sensor specific information to the framework like in our case we need to
   give:
   • Heart rate sensor:
   • Sensor type: Camera
   • Sampling frequency: Specify the frame rate, duration you want to use (e.g., 30 fps, 45 sec –
   depends on the device used for recording & the code)
   • Respiratory rate sensor:
   • Sensor type: Accelerometer
   • Sampling frequency: Specify the duration you want to use (e.g., 45 sec)
2. Data processing:
   • Specify the algorithms needed for processing the raw sensor data:<br/>
   In our case it was HeartRate algo & RespiratoryRate algo in the repository
3. User interface:
   • Specify UI elements for the smartphone application:
   • Buttons: Start/stop buttons, input symptoms page
   • Text views: Display heart rate and respiratory rate
   • Graphs: We can event show trends of heart rate and respiratory rate over time (we can use the
   dB and use some inbuilt library to generate a graph or a good pictorial representation)
4. Data storage:
   • Specify that you need a database to store the data generated:<br/>
   In our case it was:
   • Heart rate measurements
   • Respiratory rate measurements
   • User-input symptoms
5. Smart phone specifications:
   • Platform: Android
   • Required permissions for the app to function: Camera, audio, accelerometer

By providing these specifications the Health-Dev framework should be able to generate the basic code
needed for the application. The framework would handle the low-level sensor interactions, data
processing, and basic UI generation based on the specifications. We might need to write/adjust some
of the elements according to our personal requirements, but this can help in reducing a lot of
effort for the developers and would lead to less errors overall.

---

**Question: In Project 1 you have stored the user’s symptoms data in the local server. Using the
bHealthy application suite how can you provide feedback to the user and develop a novel application
to improve context sensing and use that to generate the model of the user? (15 points)**

Answer:
Project1 is the basic version of the bHealthy suite, which only generates heartRate and
respiratoryRate and takes in the symptoms from the user itself whereas the bHealthy app tries to
make itself as an application aware adaption app, adapting itself according to the user mood
changes. We can enhance our app in numerous ways:

Ways to make it a novel application:

1. Integrate additional sensors:
   In addition to the heartRate and respiratoryRate measurements from our existing app, we can
   incorporate EEG sensors as used in bHealthy. This will allow us to collect more physiological
   data.
2. Implement an assessment application:
   Develop an app that uses the collected physiological signals to detect the user's mental and
   physical state. For example, we can use EEG data to assess mental states like boredom,
   excitement, meditation, and frustration, like bHealthy's approach.
3. Create an activity suggestion module:
   Based on the results, we can implement a system that suggests activities to improve the user’s
   wellbeing. This could include recommending exercises, relaxation techniques.
4. Develop training applications:
   We can create specific apps that target different aspects of wellness. For example: A physical
   activity app like PETPeeves or a mental health app like BrainHealth. Or we can even integrate it
   with other apps which promote healthy activities.
5. Implement context sensing:
   We can use the smartphone's sensors (GPS) to detect the user's location, to know about the user
   context and later integrate it with AI models to improve other detections.
6. Generate a user model:
   We can combine the physiological data, symptoms, context information, and user interactions with
   the app to create a comprehensive user model. It can be continuously updated to provide
   personalized feedback and suggestions. And AI being a boom, we can look for the models or
   generate new ones which can help us in determining future moods based on the suggested
   activities (there is a huge scope here).

Ways to provide feedback to the user:

1. Provide wellness reports:
   Develop a wellness report generator that compiles data from all the applications. This can
   provide users with an overall view of their health status and progress over time. We can even
   integrate the data from the medical reports by asking the users to upload it.
2. Enhance data visualization:
   Create intuitive visualizations of the user's health data, like in the fitbit or amazefit apps.
   This can help users better understand their health patterns and motivate behavior change.
3. Gamification elements:
   We can incorporate elements like badges, points, or progress visualizations to increase user
   engagement and motivation.

By implementing these features, we can transform our existing symptom-tracking app into a
comprehensive wellness application suite that provides personalized feedback, improves context
sensing, and generates a detailed model of the user's health and behavior patterns. This approach
can lead to better health outcomes for users.

---

**Question: A common assumption is mobile computing is mostly about app development. After
completing Project 1 and reading both papers, have your views changed? If yes, what do you think
mobile computing is about and why? If no, please explain why you still think mobile computing is
mostly about app development, providing examples to support your viewpoint (10 points)**

Answer:
After working on the Project1, my views changed about mobile computing not just being app
development, and after reading the research papers my views are now solidified. The following are
the reasons why I feel so:

1. Integration of hardware and software:
   The bHealthy system described in the first paper demonstrates how mobile computing involves a
   tight integration of hardware (sensors like ECG, EEG, accelerometers) with software applications.
   This goes beyond simple app development (which was my view like simple applications: ToDo
   application, etc.)
2. Real-time processing and feedback:
   The emphasis on real-time processing of medical data and providing immediate feedback
   demonstrates that mobile computing often deals with time-sensitive applications, requiring
   specialized knowledge of efficient algorithms and system design.
3. Interdisciplinary nature:
   The bHealthy system combines knowledge from medical field, human-computer interaction, and
   software engineering. This does feel something which I never considered and even this is not
   quite in the trend which may change in the next decade.
4. Research and innovation:
   Both papers describe novel systems that push the boundaries of what's possible with mobile
   devices. This research-oriented aspect of mobile computing goes beyond creating apps for existing
   use cases.

In conclusion, while app development is certainly a part of mobile computing, these research papers
and Project 1 have changed my mind and have shown that the field is much broader and in the coming
decade it might have such apps which integrate the user’s health.

---