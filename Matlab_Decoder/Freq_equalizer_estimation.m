function [H, G] = Freq_equalizer_estimation(tx,rx, nbin1, nbin2)
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here
    if(length(rx)~= length(tx))
        disp('warinig tx and rx different size')
        h= [];
        return;
    end
    fft_tx = fft(tx);
    fft_rx  = fft(rx);
    % G = X(W)/Y(W)
    
%     hold on
%     delta = 39 - 21;
%     if(real(fft_tx(nbin1+delta)) < 0)
%         scatter(real(fft_rx(nbin1+delta)), imag(fft_rx(nbin1+delta)), 'ro')
%     else
%         scatter(real(fft_rx(nbin1+delta)), imag(fft_rx(nbin1+delta)), 'bx')
%     end
%     xlim([-2 2])
%     ylim([-2 2])
%     plot([-2, 2], [0, 0], '--k')
%     plot([0, 0], [-2, 2], '--k')
    
    H = fft_rx(nbin1:nbin2)./fft_tx(nbin1:nbin2);
    G = fft_tx(nbin1:nbin2)./fft_rx(nbin1:nbin2);
    ss = 1;
end