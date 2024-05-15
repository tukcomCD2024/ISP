package com.project.how.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.R
import com.project.how.data_class.dto.GetFlightOffersResponseElement
import com.project.how.databinding.RoundTripAirplaneListItemBinding
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class RoundTripAirplaneListAdapter(private val context: Context, private val data : ArrayList<GetFlightOffersResponseElement>, private val onItemClickListener : OnItemClickListener) : RecyclerView.Adapter<RoundTripAirplaneListAdapter.ViewHolder>(){
    private val hearts = MutableList<Boolean>(data.size) { false }

    inner class ViewHolder(val binding : RoundTripAirplaneListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : GetFlightOffersResponseElement, position: Int){
            val abroadDepartureDateTime = getDateTime(data.abroadDepartureTime)
            val abroadArrivalDateTime = getDateTime(data.abroadArrivalTime)
            val abroadDuration = getDuration(data.abroadDuration)
            val homeDepartureDateTime = getDateTime(data.homeDepartureTime)
            val homeArrivalDataTime = getDateTime(data.homeArrivalTime)
            val homeDuration = getDuration(data.homeDuration)
            val formattedPrice = getFormattedNumber(data.totalPrice)
            
            binding.price.text = context.getString(R.string.price, formattedPrice)

            binding.abroadDate.text = abroadDepartureDateTime[0]
            binding.abroadTime.text = context.getString(
                R.string.flights_time,
                abroadDepartureDateTime[1],
                abroadArrivalDateTime[1]
            )
            binding.abroadAirport.text = context.getString(
                R.string.fligths_airport,
                data.departureIataCode,
                data.arrivalIataCode
            )
            binding.abroadNonStop.text = if (data.nonstop) context.getString(R.string.non_stop) else context.getString(R.string.stop, data.transferCount.toString())
            binding.abroadDuration.text = abroadDuration

            binding.homeDate.text = homeDepartureDateTime[0]
            binding.homeTime.text = context.getString(
                R.string.flights_time,
                homeDepartureDateTime[1],
                homeArrivalDataTime[1]
            )
            binding.homeAirport.text = context.getString(
                R.string.fligths_airport,
                data.arrivalIataCode,
                data.departureIataCode
            )
            binding.homeNonStop.text = if (data.nonstop) context.getString(R.string.non_stop) else context.getString(R.string.stop, data.transferCount.toString())
            binding.homeDuration.text = homeDuration

            binding.heart.setOnClickListener {
                if (hearts[position]){
                    hearts[position] = !hearts[position]
                    unLike(binding)
                }else{
                    hearts[position] = !hearts[position]
                    like(binding)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        RoundTripAirplaneListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val d = data[position]
        holder.bind(d, position)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClickerListener(d)
        }
    }

    private fun like(binding : RoundTripAirplaneListItemBinding){
        binding.heart.setImageResource(R.drawable.icon_heart_red_fill_white_line)
    }

    private fun unLike(binding : RoundTripAirplaneListItemBinding){
        binding.heart.setImageResource(R.drawable.icon_heart_red_fill_white_line)
    }

    private fun getDateTime(timeDate : String) : List<String>{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val localDateTime = LocalDateTime.parse(timeDate, formatter)
        val dateFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        return listOf(localDateTime.format(dateFormatter), localDateTime.format(timeFormatter))
    }

    private fun getDuration(duration : String) : String {
        val hm = duration.split(":")
        return context.getString(R.string.fligths_duration, hm[0], hm[1])
    }

    private fun getFormattedNumber(price : Long) : String = NumberFormat.getNumberInstance(Locale.US).format(price)

    interface OnItemClickListener{
        fun onItemClickerListener(data : GetFlightOffersResponseElement)
        fun onHeartClickerListener()
    }
}