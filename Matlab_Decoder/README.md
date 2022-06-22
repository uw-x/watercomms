# Matlab decoder for underwater messaging system

## Overview

### ./sending_signal
It contains the original sending preamble and sending bits with differen convolutional code rate 

### ./raw_data
We have some sample data collected from the real world. For each setting, there are around 100-130 packets.

(1) "./raw_data/envs" is the data colected in different environments: (1) a long bridge in the Washington Lake (2) gas station park (3) a fishing platform of Green lake (4) a harbor with deep water (~10m deep)

(2) "./raw_data/dis/"  is the data colected in a fishing platform of Green Lake with different horizontal distance (5m, 10m, 20m, 30m)

(3) "./raw_data/depth" is colected in a deep and busy harbor near the Lake Union Bay with different depth (2m, 5m, 7m)

(4) "./raw_data/motion" is colected in a fishing platform of Green Lake with different motion speed

(5) "./raw_data/rot" is colected in a long bridge in the Washington Lake with different phone orientation

(6) "./raw_data/Ns" is colected in a Green Lake with different OFDM symbol length (960, 1920, 4800). Be careful, when change to differnt Ns, also change the Ns in line 18

### Matlab decoder
(1) Decoder_offline.m is the main function code for our decoding and demodulation process

(2) find_symbol.m, seekback.m, channel_look_back.m are used for finding the begin of the recv symbol 

(3) Time_equalizer_estimation.m, Time_equalizer_recover.m, symbol_decode_time.m are used for time-domain equalizer and symbol recover

(3) Freq_equalizer_estimation.m, symbol_decode_freq.m are used for frequency-domain equalizer and symbol recover

(4) viterbi_decode.m is the Viterbi process for the convolutional decoding

(5) hmap.m is used for visuliation the error bit 


## Running instruction
We run our code in Matlab 2021b Version

Run code "Decoder_offline.m", the PER and BER will be printed in the console and a CDF figure for data rate will come out.

change folder_name in line 6 to select which raw data to load

change pakcets_to_check in line 7 to select the packet index to be decoded

change visual_debug in line 8 to 1, then the figure of time-domain signal, spectrum of recv signal, equilizer, error bits will be visualized and more debug information will be printed. (Caution!!: do not enbale visual_debug when pakcets_to_check contains large number of packets, because the visulaization process may occupy too much CPU and memory. Instead, you can set pakcets_to_check to index of only one packet and then enable visual_debug.)


