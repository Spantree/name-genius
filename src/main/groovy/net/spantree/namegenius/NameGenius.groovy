package net.spantree.namegenius

import groovy.transform.CompileStatic

import java.nio.ByteBuffer
import java.security.MessageDigest

@CompileStatic
class NameGenius {
    static final int RETRY_CAP = 1000

    NameList femaleFirstNames
    NameList maleFirstNames
    NameList lastNames
    NameList jobNames
    NameList companyNames
    Map<String, NameList> nameToAvatarMap
    Double percentageFemale
    Set<ByteBuffer> previousNameHashes = []

    /**
    * Creates a new NameGenius object with the given percentageFemale, and a map of first name -> picture
     *
     * @param percentageFemale This sets the percentage of females vs males generated on average
     * @param nameToAvatarMap This points to a particular file which contains a list in the form
     * name avatarLocation
     *
     * @return A NameGenius object
    */
    NameGenius(double percentageFemale = 0.5, String nameToAvatarMap = "avatarMappings.txt") {
        this.femaleFirstNames = new NameList('firstNames_female.txt')
        this.maleFirstNames = new NameList('firstNames_male.txt')
        this.lastNames = new NameList('lastNames.txt')
        this.jobNames = new NameList('jobNames.txt')
        this.companyNames = new NameList('companyNames.txt')
        this.nameToAvatarMap = generateNameToAvatarMap(nameToAvatarMap)
        this.percentageFemale = percentageFemale
    }

    /**
     * Generates a person with a name unique from previously-generated names
     *
     * @return A person with a random combination of gender, first name and last name
     */
    Person generate(avatar = false) {
        Gender gender = (RandomSingleton.instance.nextDouble() < percentageFemale) ? Gender.Female : Gender.Male
        (gender == Gender.Female ? generateFemale(avatar) : generateMale(avatar))
    }

    Person generateFemale(avatar = false) {
        generateForGender(Gender.Female, avatar)
    }

    Person generateMale(avatar = false) {
        generateForGender(Gender.Male, avatar)
    }

    Employee generateEmployee(avatar = false) {
        def person = generate(avatar)
        generateEmployeeForPerson(person)
    }

    Employee generateEmployeeForPerson(Person person) {
        def jobName = jobNames.pickRandom()
        def companyName = companyNames.pickRandom()

        new Employee(firstName: person.firstName, lastName: person.lastName, gender: person.gender, avatarUrl: person.avatarUrl, jobName: jobName, companyName: companyName)
    }

    boolean nameAlreadyExists(Person person) {
        !previousNameHashes.add(person.sha1NameHash)
    }

    Employee generateEmployeeByName(String firstName, String lastName = "Last") {
        def jobName = jobNames.pickRandom()
        def companyName = companyNames.pickRandom()

        def avatarUrl
        if (nameToAvatarMap.containsKey(firstName)) {
            avatarUrl = nameToAvatarMap.get(firstName).pickRandom()
        } else {
            MessageDigest digest = MessageDigest.getInstance("MD5")
            def sampleEmail = firstName.toLowerCase().trim() + "@gravatar.com"
            digest.update(sampleEmail.bytes)

            BigInteger big = new BigInteger(1, digest.digest())
            String md5 = big.toString(16).padLeft(32, "0")

            avatarUrl = "http://gravatar.com/avatar/" + md5
        }
        new Employee(firstName: firstName, lastName: lastName, avatarUrl: avatarUrl, jobName: jobName, companyName: companyName)

    }

    private Person generateForGender(Gender gender, avatar = false) {
        Person person = null
        NameList firstNameList = (gender == Gender.Female ? femaleFirstNames : maleFirstNames)
        int retries = 0
        while (retries++ < RETRY_CAP) {
            String firstName = firstNameList.pickRandom()
            String lastName = lastNames.pickRandom()
            person = new Person(firstName: firstName, lastName: lastName, gender: gender)
            if (!nameAlreadyExists(person)) {
                if (avatar) {
                    if (nameToAvatarMap.containsKey(firstName)) {
                        person.avatarUrl = nameToAvatarMap.get(firstName).pickRandom()
                        break
                    }
                } else {
                    break
                }
            }

        }
        if (retries >= RETRY_CAP) {
            throw new IllegalStateException("Tried to generate unique name ${retries} times, retry cap of ${RETRY_CAP} exceeded")
        }
        person
    }

    private Map<String, NameList> generateNameToAvatarMap(String file) {

        def stream = getClass().getResourceAsStream(file)
        def map = new HashMap<String, NameList>()
        stream.eachLine {
            String line ->
                def firstName = line.split()[0]
                def avatarUrl = line.split()[1]
                if (!map.containsKey(firstName)) {
                    map.put(firstName, new NameList())
                }
                map.get(firstName).add(avatarUrl)
        }
        map
    }

    void setNameToAvatarMap(String fileName) {
        this.nameToAvatarMap = generateNameToAvatarMap(fileName)
    }

    void setFemaleFirstNames(String fileName) {
        this.femaleFirstNames = new NameList(fileName)
    }

    void setMaleFirstNames(String fileName) {
        this.maleFirstNames = new NameList(fileName)
    }

    void setLastNames(String fileName) {
        this.lastNames = new NameList(fileName)
    }

    void setJobNames(String fileName) {
        this.jobNames = new NameList(fileName)
    }

    void setCompanyNames(String fileName) {
        this.companyNames = new NameList(fileName)
    }

    void setPercentageFemale(Double femaleToMaleRatio) {
        this.percentageFemale = femaleToMaleRatio
    }
}
