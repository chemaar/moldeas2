program fact
  real fvalue
  integer n
  print *, "Which is the number you want to calculate the factorial?"
  read *, n
  ! fact(n) = n * fact (n-1)
  if (n < 0) then 
    print *, "You cannot calculate the factorial of negative numbers"
  else if (n>=0 .AND. n<=1) then
    fvalue = 1
  else
    fvalue = 1
    do i = 2, n
      fvalue = i * fvalue
    end do
  end if
  print *, "The result is ", fvalue
end
