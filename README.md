# Infinite Recharge 2020

This is the official code of team Spikes#2212 for the 2020 FRC season.



## Code Conventions 

#### General 

1. This project is developed according to the standard java code style convention, described [here](https://google.github.io/styleguide/javaguide.html).
2. Each branch is named in `lower-case`. 
3. Commit messages are written in `lower case`.


#### Development

##### Feature Branches
This code is developed using the feature branches workflow. 

Each feature is developed inside it's own branch, which is merged into dev after CR and testing. <br> 

##### Testing 

Each feature branch should be forked by a `feature-branch-name-testing` branch, in which all necessary testing code is added.
all the code fixed should be commit into this branch.
After the code passes testing all the commits from the `-testing` branch are squashed,
and then it is merged with the feature branch.

##### DEV Branch

All feature branches that passed testing successfully are later merged into the `dev` branch, 
which is in turn merged into `master` after passing complete testing and integration.  
