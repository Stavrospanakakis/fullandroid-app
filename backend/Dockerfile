FROM php:7.3.3-apache

# Install required packages
RUN apt-get update && apt-get upgrade -y
RUN docker-php-ext-install mysqli
RUN apt-get update && apt-get install -y --no-install-recommends \
  python3-minimal python3-setuptools python3-pip netcat libsm6 libxext6 libxrender-dev && \
  rm -f /usr/bin/python && ln -s /usr/bin/python3 /usr/bin/python && \
  rm -f /usr/bin/pip && ln -s /usr/bin/pip3 /usr/bin/pip 

RUN pip3 install opencv-python==4.2.0.34 numpy Pillow 


# Add vhosts and enable sites.
COPY vhosts /etc/apache2/sites-available
RUN cd /etc/apache2/sites-available && a2ensite * && a2dissite default-ssl 000-default


EXPOSE 80
