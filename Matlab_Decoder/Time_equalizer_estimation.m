function [g] = Time_equalizer_estimation(tx,rx,tap_num)
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here
    lambda = 1e-5; %1e-6
    if((size(tx, 1)+ tap_num - 1) ~= size(rx, 1))
        disp('warinig tx and rx different size')
        g= [];
        return;
    end
    if(tap_num > size(tx, 1))
        disp('tap number is too large')
        g= [];
        return;
    end
    
    symbols_num = size(tx, 2);
   
    P = size(tx, 1);
    L= tap_num;
    M = zeros(P*symbols_num, L);
    Y = [];
    ii = 1;
    for a=1:symbols_num
        for b = 1:P
            M(ii, :) = rx(b:L+b-1, a);
            ii = ii + 1;
        end
    end
    
    for i = 1:symbols_num
        Y= [Y; tx(:, i)];
    end

    g = pinv((M'*M) + lambda*eye(L))*M'*Y;

end