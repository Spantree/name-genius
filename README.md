# Name Genius

A Java/Groovy library that generates randomized, unique first and last names with gender.  Each generated name is
guaranteed unique for the lifecycle of a given NameGenius object.  It will regenerate if it detects a collision. If
generating for both genders, there should be over 16 million unique names possible.

## Basic

### Usage

```groovy
import net.spantree.NameGenius
import net.spantree.Person

NameGenius genius = new NameGenius()
def people = []
10.times {
    people << genius.generate()
}
people.each { Person person ->
    println "${person.firstName} ${person.lastName} (${person.gender})"
}
```

### Output

```
Leone Noseworthy (Female)
Elmer Kuruppillai (Male)
Annice Gaebel (Female)
Chauncey Ficken (Male)
Nicola Whiskin (Female)
Davis Wurtz (Male)
Otelia Raab (Female)
Hunter Callender (Male)
Scot Thibeault (Male)
Lise Tresrch (Female)
```

## Gender-Specific

### Usage

```groovy
import net.spantree.NameGenius
import net.spantree.Person

NameGenius genius = new NameGenius()

Person male = genius.generateMale()
println "${male.firstName} ${male.lastName} (${male.gender})"

Person female = genius.generateFemale()
println "${female.firstName} ${female.lastName} (${female.gender})"
```

### Output

```
Frank Hanzel (Male)
Marielle Fastfeat (Female)
```

## Copyright

First and last name lists provided by [Scrapmaker](http://scrapmaker.com/dir/names).

Job name list provided by [U.S. Bureau of Labor Statistics](http://www.bls.gov/ooh/print/a-z-index.htm)

Company name list provided by [Freebase](http://www.freebase.com/fictional_universe/fictional_employer?instances=)

Default avatars provided by [Github's Octodex](http://octodex.github.com/), with randomization provided by [Random-Octocat](https://github.com/elcuervo/random-octocat)

Copyright 2013, Cedric Hurst and Spantree Technology Group, LLC.

## License

```
This software is licensed under the Apache 2 license, quoted below.

Copyright 2013 Cedric Hurst and Spantree Technology Group <http://www.spantree.net>

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```
