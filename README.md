# flatten-array-strutcs

convert spark data as columns (old terminology crosstab/pivot)

Schema
------

data:

{"id":1,"dept":[{"department":"IT","emp_count":250,"expenses":45000}, {"department":"SALES","emp_count":20, "expenses":32000}, {"department":"MARKETING","emp_count":35,"expenses":33000}]}
{"id":2,"dept":[{"department":"OPERATIONS","emp_count":300,"expenses":24000}, {"department":"FINANCE","emp_count":5,"expenses":19000}, {"department":"HR","emp_count":32,"expenses":17000}]}


## Usage

    $ lein do clean, run 
    

## Options

FIXME: listing of options this app accepts.

## Examples

output 

{"id":1,"IT_department":"IT","IT_emp_count":250,"IT_expenses":45000,"SALES_department":"SALES","SALES_emp_count":20,"SALES_expenses":32000,"MARKETING_department":"MARKETING","MARKETING_emp_count":35,"MARKETING_expenses":33000}
{"id":2,"OPERATIONS_department":"OPERATIONS","OPERATIONS_emp_count":300,"OPERATIONS_expenses":24000,"FINANCE_department":"FINANCE","FINANCE_emp_count":5,"FINANCE_expenses":19000,"HR_department":"HR","HR_emp_count":32,"HR_expenses":17000}

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2018 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
