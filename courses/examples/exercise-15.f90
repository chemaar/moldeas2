program isArmstrong
 integer :: number
 integer :: result, source, remainder

 print*,"Intro a number..."
 read *, number

 source = number
 result = 0
 do while (source .NE. 0)
  remainder = mod(source,10)
  result = result + remainder**3
  source = source / 10
 end do

 print*,"The number ",number," is an Armstrong number ",  (number .EQ. result)
end program isArmstrong

