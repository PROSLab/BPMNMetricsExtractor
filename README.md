# BPMN_Metadata_Extractor
Java application that allows to extract analytical information out of BPMN models stored as .bpmn file

Basic Metric Index:
	"NAC": Number of Activation Conditions
	"NACT": Number of Activities
	"NART": Number of Artifacts
	"NASI": Number of Assignments
	"NASO": Number of Associations
	"NAUD": Number of Auditing	
	"NBEL": Number of Base Elements
	"NBEV": Number of Boundary Events
	"NBRT": Number of Business Rule Tasks
	"NCAC": Number of Call Activities
	"NCANEV": Number of Cancel Events
	"NCATEV": Number of Catch Events
	"NCBDEF": Number of Complex Behavior Definitions
	"NCCO": Number of Call Conversations
	"NCD": Number of Complex Decisions
	"NCEL": Number of Callable Elements
	"NCOCON": Number of Completion Conditions
	"NCOL": Number of Collaborations
	"NCOMEV": Number of Compensate Events
	"NCOND": Number of Conditions
	"NCONDEV": Number of Conditional Event
	"NCONDEX": Number of Condition Expressions
	"NCONV": Number of Conversations
	"NCONVAS": Number of Conversation Associations
	"NCONVL": Number of Conversation Links
	"NCONVN": Number of Conversation Nodes
	"NCORK": Number of Correlation Keys
	"NCORP": Number of Correlation Properties
	"NCORPB": Number of Correlation Property Bindings
	"NCORPRE": Number of Correlation Property Retrieval Expressions
	"NCORS": Number of Correlation Subscriptions
	"NCVAL": Number of Category Values	
	"NDA": Number of Data Associations
	"NDEF": Number of Definitions
	"NDInA": Number of Data Input Associations
	"NDO": Number of Data Objects
	"NDOC": Number of Documentations
	"NDOin": Number of Input Data Objects
	"NDOout": Number of Output Data Objects
	"NDOR": Number of Data Object References
	"NDOutA": Number of Data Output Associations
	"NDSTA": Number of Data States
	"NDSTO": Number of Data Stores
	"NEDDB": Number of Exclusive Data Based Decisions
	"NEDEB": Number of Exclusive Event Based Decisions
	"NENDEV": Number of End Events
	"NENDP": Number of End Points
	"NERR": Number of Errors
	"NERREV": Number of Error Events
	"NESC": Number of Escalations
	"NESCEV": Number of Escalation Events
	"NEV": Number of Events
	"NEVDEF": Number of Event Definitions
	"NEXP": Number of Expressions
	"NEXT": Number of Extensions
	"NEXTEL": Number of Extension Elements
	"NFDCG": Number of Flux Dividing Complex Gateways
	"NFDEBG": Number of Flux Dividing Event Based Gateways
	"NFDEXG": Number of Flux Dividing Exclusive Gateways
	"NFDIG": Number of Flux Dividing Inclusive Gateways
	"NFDPG": Number of Flux Dividing Parallel Gateways
	"NFDT": Number of Flux Dividing Tasks
	"NFJCG": Number of Flux Joining Complex Gateways
	"NFJEBG": Number of Flux Joining Event Based Gateways
	"NFJEXG": Number of Flux Joining Exclusive Gateways
	"NFJIG": Number of Flux Joining Inclusive Gateways
	"NFJPG": Number of Flux Joining Parallel Gateways
	"NFJT": Number of Flux Joining Tasks
	"NFJCG": Number of Flux Joining And Dividing Complex Gateways
	"NFJDEBG": Number of Flux Joining And Dividing EventBased Gateways
	"NFJDEXG": Number of Flux Joining And Dividing Exclusive Gateways
	"NFJDIG": Number of Flux Joining And Dividing Inclusive Gateways
	"NFJDPG": Number of Flux Joining And Dividing Parallel Gateways
	"NFJDT": Number of Flux Joining And Dividing Tasks
	"NFLEL": Number of Flow Elements
	"NFLNO": Number of Flow Nodes
	"NFOREXP": Number of Formal Expressions
	"NGA": Number of Gateways
	"NGC": Number of Global Conversations
	"NHP": Number of Human Performers
	"NIAEL": Number of Item Aware Elements
	"NICEV": Number of Intermediate Catch Events
	"NID": Number of Inclusive Decisions
	"NIMP": Number of Imports
	"NInDI": Number of Input Data Items
	"NInS": Number of Input Sets
	"NINTNO": Number of Interaction Nodes
	"NINTE": Number of Interfaces
	"NIOB": Number of Io Bindings
	"NIOS": Number of Io Specifications
	"NITEV": Number of Intermediate Throw Events
	"NL": Number of Lanes
	"NLEV": Number of Link Events
	"NLOOPCA": Number of Loop Cardinalities
	"NLOOPCH": Number of Loop Characteristics
	"NMES": Number of Messages
	"NMESEV": Number of Message Events
	"NMESFA": Number of Message Flow Associations
	"NMF": Number of Message Flows
	"NMILCH": Number of Multi Instance Loop Characteristicss
	"NMON": Number of Monitorings
	"NMT": Number of Manual Tasks
	"NOP": Number of Operations
	"NOutDI": Number of Output Data Items
	"NOutS": Number of Output Sets
	"NP": Number of Pools
	"NPG": Number of Parallel Gateways
	"NPAS": Number of Participant Associations
	"NPM": Number of Participant Multiplicities
	"NPER": Number of Performers
	"NPO": Number of Potential Owners
	"NPROC": Number of Processes
	"NPROP": Number of Properties
	"NRT": Number of Receive Tasks
	"NREL": Number of Relationships
	"NREN": Number of Renderings
	"NRES": Number of Resources
	"NRESAEX": Number of Resource Assignment Expressions
	"NRESP": Number of Resource Parameters
	"NRESPB": Number of Resource Parameter Bindings
	"NRESR": Number of Resource Roles
	"NRE": Number of Root Elements
	"NSC": Number of Scripts
	"NSCT": Number of Script Tasks
	"NSENT": Number of Send Tasks
	"NSEQF": Number of Sequence Flows
	"NSERT": Number of Service Tasks
	"NSFE": Number of Sequence Flows from Events
	"NSFG: Number of Sequence Flows from Gateways
	"NSFA": Number of Sequence Flows between Activities
	"NSI": Number of Signals
	"NSIEV": Number of Signal Event
	"NSTEV": Number of Start Events
	"NSC": Number of Sub Conversations
	"NT": Number of tasks
	"NTEV": Number of Terminate Events
	"NTEX": Number of Texts
	"NTEXA": Number of Text Annotations
	"NTEV": Number of Throw Events
	"NTC": Number of Time Cycles
	"NTDA": Number of Time Dates
	"NTDU": Number of Time Durations
	"NTEVD": Number of Timer Event Definitions
	"NTR": Number of Transactions
	"NUT": Number of User Tasks
	"NSUB": Number of Subprocesses
	
	
Advanced Metric Index:
	"CLA": Connectivity Level Between  Activities
	"CLP": Connectivity Level Between Partecipants
	"PDOPin": Proportion Of Incoming Data Objects And Total Data Objects
	"PDOPout": Proportion Of Outgoing Data Objects And Total Data Objects
	"TNT": Total Number Of Tasks
	"PDOTout": Proportion Of Data Objects As Outgoing Products
	"PLT": Proportion Of Pools Or Lanes And Activities
	"TNCS": Total Number of Collapsed Sub Processes
	"TNA": Total Number Of Activities
	"TNDO": Total NumberOfDataObjects
	"TNG": Total NumberOfGateways
	"TNEE": Total Number Of End Events
	"TNIE": Total Number Of Intermediate Events
	"TNSE": Total Number Of Start Events
	"TNE": Total Number Of Events