# Infinite Recharge 2020

This is the official code of team Spikes#2212 for the 2020 FRC season.



## Code Conventions 

#### General 

1. This project is developed according to the standard java code style convention, described [here](https://google.github.io/styleguide/javaguide.html).
2. Each branch is named in `lower-case`. 
3. Commit messages are written in `lower case`.


#### Development

#### Code Convenctions

All code in this project should be written according to the following convention, code would not be merged in case it doesn't. 

1. All classes should be written in the following order 
    1. Constant Values
    2. Singleton Initialization
    3. Class Members
    4. Constructor
    5. Methods.
2. All Singleton member values should be initialized in one line, no initializations in the constructor!
3. All `Namespace` instances should be named in `lower case`.


#### Component Naming Conventions

1. All CANBus components should be named in `lower-case` according to the following convention - `subsystem-side-controller \ index`
2. All ports in RobotMap should be named in `ALL_CAPS` according to the following convention `SUBSYSTEM_COMPONENT_INDEX`

##### Feature Branches
This code is developed using the feature branches workflow. 

Each feature is developed inside its own branch, which is merged into dev after CR and testing. <br> 

##### Testing 

Each feature branch should be forked by a `feature-branch-name-testing` branch, in which all necessary testing code is added.
all the code fixed should be commit into this branch.
After the code passes testing all the commits from the `-testing` branch are squashed,
and then it is merged with the feature branch.

##### DEV Branch

All feature branches that passed testing successfully are later merged into the `dev` branch, 
which is in turn merged into `master` after passing complete testing and integration.  
