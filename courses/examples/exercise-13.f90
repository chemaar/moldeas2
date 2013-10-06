program leapyear
 integer :: year
 logical :: isLeapYear

 print*,"Intro a year..."
 read *, year

 isLeapYear = mod (year, 4) == 0
 ! divisible by 4 and not 100
 isLeapYear = isLeapYear .AND. .NOT.(mod(year, 100) == 0.0)
 ! divisible by 4 and not 100 unless divisible by 400
 isLeapYear = isLeapYear .OR. mod(year, 400) == 0

 print*,"The year ",year," is leap year ",isLeapYear
 
end program leapyear


