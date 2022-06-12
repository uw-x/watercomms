function [tx] = Time_equalizer_recover(rx,g)
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here
    L= length(g);
    P = length(rx) - L + 1;
   
    M = zeros(P, L);

    for i=1:P
        M(i, :) = rx(i:L+i-1);
    end

    tx = M*g;
end