program swap2variables
 real x, y, z
 print *, "What are the two numbers you want to swap?"
 read *, x, y
 print *, "The values (before swapping) are ", x, y
 z = y
 y = x
 x = z
 print *, "The values (after swapping) are ", x, y
end program swap2variables