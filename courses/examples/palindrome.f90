program isPalindrome
	integer:: palindrome, num, reverse, remainder
	read *, num
	palindrome = num
	reverse = 0
	do while (palindrome /= 0)
		remainder = mod(palindrome,10)
		reverse = reverse * 10 + remainder
		palindrome = palindrome / 10	
	end do
	if (num == reverse ) then 
	    print *, "The number ", num, " is a palindrome."
	else 
	     print *, "The number ", num, " is not a palindrome."
	end if
end 
