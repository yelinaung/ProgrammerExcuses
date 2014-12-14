Programmer Execuses
===================

Simple client which uses [pe-api](https://github.com/yelinaung/pe-api). 

It's now available on Play Store

<a href="https://play.google.com/store/apps/details?id=com.yelinaung.programmerexcuses">
  <img alt="Get it on Google Play" src="https://developer.android.com/images/brand/en_generic_rgb_wo_60.png" />
</a>


![Screenshot](https://raw.githubusercontent.com/yelinaung/ProgrammerExcuses/master/screenshot.png)

It's using 
- [SwipeRefreshLayout](https://developer.android.com/reference/android/support/v4/widget/SwipeRefreshLayout.html) for swipe-down-to-refresh layout.
- [ButterKinife](https://jakewharton.github.io/butterknife) for view injections.
- [Retrofit](https://github.com/square/retrofit) for REST client.

TODO
----
- ~~"Share"~~
- Daily Notifications

Required Permissions
--------------------
`android.permission.INTERNET` - Need to access to API to fetch the message
`android.permission.ACCESS_NETWORK_STATE` - Need to access whether the host device is offline/not connected to wifi

Contributing
------------

 1. Fork it
 2. Create your feature branch (`git checkout -b my-new-feature`)
 3. Commit your changes (`git commit -am 'Added some feature'`)
 4. Push to the branch (`git push origin my-new-feature`)
 5. Create new Pull Request

License
--------

    The MIT License (MIT)

    Copyright (c) 2014 Ye Lin Aung

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
